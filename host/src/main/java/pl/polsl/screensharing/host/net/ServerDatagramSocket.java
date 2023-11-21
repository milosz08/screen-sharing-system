/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.controller.VideoCanvasController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.QualityLevel;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.UnoperableException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerDatagramSocket extends Thread {
    private final HostState hostState;
    private final VideoCanvasController videoCanvasController;

    private boolean isSendingData;
    private DatagramSocket datagramSocket;
    private Cipher cipher;
    private QualityLevel qualityLevel;

    private static final int PACKAGE_SIZE = 32_000;
    private static final int BILION = 1_000_000_000;
    private static final String TERMINATE_PACKAGE = "$$END$$";

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    public ServerDatagramSocket(
        HostWindow hostWindow, VideoCanvasController videoCanvasController
    ) {
        hostState = hostWindow.getHostState();
        this.videoCanvasController = videoCanvasController;
        qualityLevel = QualityLevel.GOOD;
        initObservables();
    }

    @Override
    public void run() {
        byte[] chunk;
        int chunkOffset = 0;
        byte[] compressedData = null;
        int unprocessedDataLength = 0;

        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0, logTimer = 0;
        long sendBytes = 0;

        log.info("Started datagram thread with TID {}", getName());
        while (isSendingData) {
            currentTime = System.nanoTime();
            timer += (currentTime - lastTime);
            logTimer += (currentTime - lastTime);
            lastTime = currentTime;
            try {
                if (compressedData == null) {
                    compressedData = loadImage();
                    unprocessedDataLength = compressedData.length;
                }
                if (unprocessedDataLength > PACKAGE_SIZE) {
                    // prześlij tylko część
                    chunk = new byte[PACKAGE_SIZE];
                    System.arraycopy(compressedData, chunkOffset, chunk, 0, PACKAGE_SIZE);

                    final long encryptedChunkLength = sendEncryptedPackage(chunk);

                    sendBytes += encryptedChunkLength;
                    unprocessedDataLength -= PACKAGE_SIZE;
                    chunkOffset += PACKAGE_SIZE;
                } else {
                    // przeslij jeden pakiet (lub ostatni pakiet)
                    chunk = new byte[unprocessedDataLength];
                    System.arraycopy(compressedData, chunkOffset, chunk, 0, unprocessedDataLength);

                    final long encryptedChunkLength = sendEncryptedPackage(chunk);
                    final long terminatePackageLength = sendPackage(TERMINATE_PACKAGE.getBytes());

                    sendBytes += (encryptedChunkLength + terminatePackageLength);
                    compressedData = null;
                    chunkOffset = 0;
                }
                // opóźnij przesyłanie pakietów (UDP przy zbyt szybkim wysyłaniu
                // ignoruje pakiety i po stronie klienta pojawiają się artefakty w obrazie
                sleep(5);
            } catch (Exception ignored) {
            }
            if (logTimer >= BILION * 6L) {
                if (sendBytes > 0) {
                    log.info("Host datagram socket processed {} bytes", sendBytes);
                }
                logTimer = 0;
            }
            if (timer >= BILION) {
                hostState.updateSendBytesPerSec(sendBytes);
                log.debug("Host datagram socket processed {} bytes", sendBytes);
                sendBytes = 0;
                timer = 0;
            }
        }
        log.info("Stopping datagram thread with TID {}", getName());
        log.debug("Collected detatched thread with TID {} by GC", getName());
        datagramSocket.disconnect();
        datagramSocket.close();
    }

    @Override
    public synchronized void start() {
        createDatagramSocket();
        initAES();
        isSendingData = true;
        if (!isAlive()) {
            super.start();
        }
    }

    public void stopAndClear() {
        isSendingData = false;
    }

    private long sendPackage(byte[] chunk) throws Exception {
        final DatagramPacket packet = new DatagramPacket(chunk, chunk.length, InetAddress.getByName("localhost"), 7648);
        datagramSocket.send(packet);
        return chunk.length;
    }

    public long sendEncryptedPackage(byte[] chunk) throws Exception {
        final byte[] encryptedChunk = encrypt(chunk);
        return sendPackage(encryptedChunk);
    }

    private byte[] encrypt(byte[] rawData) throws Exception {
        return cipher.doFinal(rawData);
    }

    public byte[] loadImage() throws IOException {
        final ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        final ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);

        final ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next();
        final ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(qualityLevel.getJpegLevel());
        jpgWriter.setOutput(outputStream);
        jpgWriter.write(null, new IIOImage(videoCanvasController.getRawImage(), null, null), jpgWriteParam);

        final byte[] compressedData = compressed.toByteArray();

        jpgWriter.dispose();
        outputStream.close();
        compressed.close();
        return compressedData;
    }

    public void createDatagramSocket() {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            throw new UnoperableException(ex);
        }
    }

    public void initAES() {
        // tryb AES, CTR - counter mode pozwala na zaszyfrowanie danych o dowolnej długości, bez dopełnienia
        // potrzebne dla UDP, bo każda ramka musi zostać zaszyfrowana oddzielnie
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getStreamingQualityLevel$(), qualityLevel -> {
            this.qualityLevel = qualityLevel;
        });
    }
}
