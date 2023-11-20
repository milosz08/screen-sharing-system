/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.controller.VideoCanvasController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.QualityLevel;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ServerDatagramSocket extends Thread {
    private final HostState hostState;
    private final VideoCanvas videoCanvas;
    private final VideoCanvasController videoCanvasController;

    private DatagramSocket datagramSocket;
    private Cipher cipher;
    private StreamingState streamingState;
    private QualityLevel qualityLevel;
    private final AtomicBoolean isScreenShowing;

    private static final int PACKAGE_SIZE = 32_000;
    private static final int MILION = 1_000_000_000;
    private static final String TERMINATE_PACKAGE = "$$END$$";

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    public ServerDatagramSocket(
        HostWindow hostWindow, VideoCanvas videoCanvas, VideoCanvasController videoCanvasController
    ) {
        hostState = hostWindow.getHostState();
        this.videoCanvas = videoCanvas;
        this.videoCanvasController = videoCanvasController;
        isScreenShowing = new AtomicBoolean(true);
        streamingState = StreamingState.STOPPED;
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

        while (true) {
            currentTime = System.nanoTime();
            timer += (currentTime - lastTime);
            logTimer += (currentTime - lastTime);
            lastTime = currentTime;
            try {
                if (videoCanvas == null
                    || !isScreenShowing.get()
                    || Objects.equals(streamingState, StreamingState.STOPPED)
                    || videoCanvasController.getRawImage() == null) {
                    continue;
                }
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
                Thread.sleep(5);

            } catch (Exception ignored) {
            }
            if (logTimer >= MILION * 6L) {
                log.info("Host datagram socket processed {} bytes", sendBytes);
                logTimer = 0;
            }
            if (timer >= MILION) {
                hostState.updateSendBytesPerSec(sendBytes);
                log.debug("Host datagram socket processed {} bytes", sendBytes);
                sendBytes = 0;
                timer = 0;
            }
        }
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
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isScreenShowing::set);
        hostState.wrapAsDisposable(hostState.getStreamingState$(), streamingState -> {
            this.streamingState = streamingState;
        });
        hostState.wrapAsDisposable(hostState.getStreamingQualityLevel$(), qualityLevel -> {
            this.qualityLevel = qualityLevel;
        });
    }
}
