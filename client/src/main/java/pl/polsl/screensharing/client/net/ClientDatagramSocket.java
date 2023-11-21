/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import pl.polsl.screensharing.client.controller.VideoCanvasController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.UnoperableException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class ClientDatagramSocket extends Thread {
    private final ClientState clientState;
    private final VideoCanvas videoCanvas;
    private final VideoCanvasController videoCanvasController;

    private DatagramSocket datagramSocket;
    private Cipher cipher;
    private boolean isFetchingData;

    private static final int PACKAGE_SIZE = 32_000;
    private static final int BILION = 1_000_000_000;
    private static final String TERMINATE_PACKAGE = "$$END$$";

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    public ClientDatagramSocket(
        ClientWindow clientWindow, VideoCanvas videoCanvas, VideoCanvasController videoCanvasController
    ) {
        clientState = clientWindow.getClientState();
        this.videoCanvas = videoCanvas;
        this.videoCanvasController = videoCanvasController;
        initObservables();
    }

    @Override
    public void run() {
        log.info("Started datagram thread with TID {}", getName());
        final ByteArrayOutputStream receivedDataBuffer = new ByteArrayOutputStream();
        byte[] receiveBuffer = new byte[PACKAGE_SIZE];

        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0, logTimer = 0;
        long recvBytes = 0;

        while (isFetchingData) {
            currentTime = System.nanoTime();
            timer += (currentTime - lastTime);
            logTimer += (currentTime - lastTime);
            lastTime = currentTime;
            try {
                final Dimension size = videoCanvas.getSize();
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                recvBytes += receivePacket.getLength();

                if (new String(receivePacket.getData(), 0, receivePacket.getLength()).equals(TERMINATE_PACKAGE)) {
                    final byte[] receivedData = receivedDataBuffer.toByteArray();
                    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedData);
                    final BufferedImage receivedImage = ImageIO.read(byteArrayInputStream);
                    final BufferedImage renderedImage = Scalr.resize(receivedImage, size.width, size.height);
                    videoCanvasController.setReceivedAndRenderedImage(receivedImage, renderedImage);
                    byteArrayInputStream.close();
                    receivedDataBuffer.reset();
                } else {
                    final byte[] decrypted = decrypt(Arrays.copyOf(receivePacket.getData(), receivePacket.getLength()));
                    receivedDataBuffer.write(decrypted, 0, decrypted.length);
                }
            } catch (Exception ex) {
                receivedDataBuffer.reset();
            }
            videoCanvas.repaint();
            if (logTimer >= BILION * 6L) {
                if (recvBytes > 0) {
                    log.info("Client datagram socket processed {} bytes", recvBytes);
                }
                logTimer = 0;
            }
            if (timer >= BILION) {
                clientState.updateRecvBytesPerSec(recvBytes);
                log.debug("Client datagram socket processed {} bytes", recvBytes);
                recvBytes = 0;
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
        isFetchingData = true;
        createDatagramSocket();
        initAES();
        if (!isAlive()) {
            super.start();
        }
    }

    public void stopAndClear() {
        isFetchingData = false;
    }

    private byte[] decrypt(byte[] encryptedData) throws Exception {
        return cipher.doFinal(encryptedData);
    }

    public void createDatagramSocket() {
        try {
            datagramSocket = new DatagramSocket(7648);
            datagramSocket.setSoTimeout(1000);
        } catch (SocketException ex) {
            throw new UnoperableException(ex);
        }
    }

    public void initAES() {
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getVisibilityState$(), visibilityState -> {
            isFetchingData = visibilityState.equals(VisibilityState.VISIBLE);
        });
    }
}
