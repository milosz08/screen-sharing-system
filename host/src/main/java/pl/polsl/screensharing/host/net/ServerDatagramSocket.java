/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import pl.polsl.screensharing.host.controller.VideoCanvasController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.QualityLevel;
import pl.polsl.screensharing.host.state.StreamingState;
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
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerDatagramSocket extends Thread {
    private final HostWindow hostWindow;
    private final HostState hostState;
    private final VideoCanvasController videoCanvasController;

    private boolean isSendingData;
    private DatagramSocket datagramSocket;
    private Cipher cipher;
    private QualityLevel qualityLevel;

    private static final int PACKAGE_SIZE = 32_768; // 32kb
    private static final int BILION = 1_000_000_000;
    private static final int MAX_FRAME_WIDTH = 1280;
    private static final int MAX_FRAME_HEIGHT = 720;

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    public ServerDatagramSocket(
        HostWindow hostWindow, VideoCanvasController videoCanvasController
    ) {
        this.hostWindow = hostWindow;
        hostState = hostWindow.getHostState();
        this.videoCanvasController = videoCanvasController;
        qualityLevel = QualityLevel.GOOD;
        initObservables();
    }

    @Override
    public void run() {
        byte[] chunk; // pakiet do przesłania
        int chunkOffset = 0; // przesunięcie pakietowe
        byte[] compressedData = null; // skompresowany strumień bajtów (klatka)
        int unprocessedDataLength = 0; // długość nieprzetworzonych danych
        byte countOfPackages = 0; // liczba przesłanych pakietów na jedną klatkę
        byte packageIteration = 1; // iterator przesłanych pakietów
        final int debugBytesLength = 3; // ilość bajtów debugujących

        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0, logTimer = 0;
        long sendBytes = 0;

        // Wątek działa w pętli dopóki istnieje sesja UDP. Kolejne przebiegi pętli to wysyłanie kolejnych to fragmentów
        // jednej klatki obrazu w formie pakietów po ~32kb + 3 bajty debugujące definiujące ilość fragmentów na jedną
        // klatkę, indeks fragmentu oraz czy jest to fragment terminalny. Wartości te potrzebne są do korekcji błędów
        // po stronie odbiorcy.

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
                    countOfPackages = (byte) Math.ceil((double) compressedData.length / PACKAGE_SIZE);
                }
                // przesyłaj pakiety dopóki ilość nieprzetworzonych bajtów będzie większa od rozmiaru ramki bez
                // bajtów debugujących
                if (unprocessedDataLength > PACKAGE_SIZE - debugBytesLength) {
                    // prześlij fragment obrazu (jeden pakiet, rozmiar ramki ~32kb (plus bajty debugujące)
                    chunk = new byte[PACKAGE_SIZE];
                    chunk[0] = countOfPackages;
                    chunk[1] = packageIteration;
                    chunk[2] = 0; // fragment
                    // kopiowanie strumienia bajtów JPEG do chunka z przesunięciem o już przetworzone pakiety oraz
                    // 3 pakiety debugujące
                    System.arraycopy(compressedData, chunkOffset, chunk, debugBytesLength,
                        PACKAGE_SIZE - debugBytesLength);
                    final long encryptedChunkLength = sendEncryptedPackage(chunk);

                    sendBytes += encryptedChunkLength;
                    unprocessedDataLength -= (PACKAGE_SIZE - debugBytesLength);
                    chunkOffset += (PACKAGE_SIZE - debugBytesLength);
                    packageIteration++;
                } else {
                    // przeslij jeden pakiet (lub ostatni pakiet)
                    chunk = new byte[unprocessedDataLength + debugBytesLength];
                    chunk[0] = countOfPackages;
                    chunk[1] = packageIteration;
                    chunk[2] = 1; // pakiet terminalny

                    System.arraycopy(compressedData, chunkOffset, chunk, debugBytesLength, unprocessedDataLength);
                    final long encryptedChunkLength = sendEncryptedPackage(chunk);

                    sendBytes += encryptedChunkLength;
                    compressedData = null;
                    chunkOffset = 0;
                    packageIteration = 1;
                }
                sleep(1);
            } catch (SocketTimeoutException | PortUnreachableException ex) {
                JOptionPane.showMessageDialog(hostWindow, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                log.error("Unexpected network error. Cause: {}", ex.getMessage());
                break;
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
        hostState.updateStreamingState(StreamingState.STOPPED);
    }

    @Override
    public synchronized void start() {
        createDatagramSocket();
        initAES();
        isSendingData = true;
        if (!isAlive()) {
            setName("Thread-UDP-" + getId());
            super.start();
        }
    }

    public void stopAndClear() {
        isSendingData = false;
    }

    private long sendPackage(byte[] chunk) throws Exception {
        final DatagramPacket packet = new DatagramPacket(chunk, chunk.length, InetAddress.getByName("192.168.0.102"), 7648);
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
        BufferedImage rawImage = videoCanvasController.getRawImage();

        final ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next();
        final ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(qualityLevel.getJpegLevel());
        jpgWriter.setOutput(outputStream);
        if (rawImage.getWidth() > MAX_FRAME_WIDTH || rawImage.getHeight() > MAX_FRAME_HEIGHT) {
            final int newHeight = (int) ((double) MAX_FRAME_WIDTH / rawImage.getWidth() * rawImage.getHeight());
            rawImage = Scalr.resize(rawImage, MAX_FRAME_WIDTH, newHeight);
        }
        jpgWriter.write(null, new IIOImage(rawImage, null, null), jpgWriteParam);

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
