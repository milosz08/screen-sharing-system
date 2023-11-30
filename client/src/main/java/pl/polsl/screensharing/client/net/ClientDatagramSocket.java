/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import pl.polsl.screensharing.client.controller.BottomInfobarController;
import pl.polsl.screensharing.client.controller.VideoCanvasController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.UnoperableException;
import pl.polsl.screensharing.lib.net.AbstractDatagramSocketThread;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import static pl.polsl.screensharing.lib.SharedConstants.BILION;
import static pl.polsl.screensharing.lib.SharedConstants.PACKAGE_SIZE;

@Slf4j
public class ClientDatagramSocket extends AbstractDatagramSocketThread {
    private final ClientState clientState;
    private final ClientWindow clientWindow;
    private final VideoCanvas videoCanvas;
    private final VideoCanvasController videoCanvasController;

    private VisibilityState visibilityState;

    public ClientDatagramSocket(
        ClientWindow clientWindow, VideoCanvas videoCanvas, VideoCanvasController videoCanvasController
    ) {
        super();
        this.clientWindow = clientWindow;
        clientState = clientWindow.getClientState();
        this.videoCanvas = videoCanvas;
        this.videoCanvasController = videoCanvasController;
        visibilityState = VisibilityState.WAITING_FOR_CONNECTION;
        initObservables();
    }

    @Override
    public void run() {
        log.info("Started datagram thread with TID {}", getName());
        final ByteArrayOutputStream receivedDataBuffer = new ByteArrayOutputStream();

        final int debugBytesLength = 3; // ilość bajtów debugujących
        byte[] receiveBuffer = new byte[PACKAGE_SIZE * 2]; // bufor na dane przychodzące (dane + bufor debugujący)
        byte[] rawDataBuffer; // bufor na surowe dane JPEG
        byte countOfPackages; // liczba pakietów uzyskana przez obiornik
        byte packageIteration; // iterator pakietów uzyskany przez obiornik
        byte realPackagesIteration = 1; // ilość przebiegów pętli po pakiety (rzeczywista pobrana ilość)
        boolean isTerminated;
        boolean isCorrupted = false;

        // Wątek odbierający dane nadawane na kanał UDP przez hosta. Posiada prosty system korekcji błędów. Główna pętla
        // co iteracje pobiera kolejne paczki nadsyłane przez hosta. Z paczek ~32kb pobierany jest 3 bajtowy ciąg
        // debugujący oraz pozostałe bajty (strumień JPEG). Dane w ciągu debugującym weryfikują poprawność pod względem:
        // - ilości paczek na jedną klatkę
        // - kolejności paczek
        // Jeśli zostanie wykryty problem z ilością paczek na jedną klatkę lub paczki będa w złej kolejności, bufor jest
        // odrzucany a klatka nie jest renderowana.

        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0, logTimer = 0;
        long recvBytes = 0;
        int corruptedFrames = 0;

        while (isThreadActive) {
            currentTime = System.nanoTime();
            timer += (currentTime - lastTime);
            logTimer += (currentTime - lastTime);
            lastTime = currentTime;
            try {
                final Dimension size = videoCanvas.getSize();
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                recvBytes += receivePacket.getLength();

                // odkodowanie danych przy użyciu klucza AES (UWAGA! Istotnia jest metoda Arrays.copyOf, ponieważ bez
                // niej AES rozpoznawał ciąg bajtów jako nieprawidłowy ponieważ dodawał swoje dopełnienie do niepełnej
                // klatki zamiast ustawić tam zera
                final byte[] decrypted = cryptoSymmetricHelper
                    .decrypt(Arrays.copyOf(receivePacket.getData(), receivePacket.getLength()));

                rawDataBuffer = new byte[decrypted.length - debugBytesLength];
                countOfPackages = decrypted[0];
                packageIteration = decrypted[1];
                isTerminated = decrypted[2] == 1;

                // kopiuj zawartość bez bajtów debugujących
                System.arraycopy(decrypted, debugBytesLength, rawDataBuffer, 0, decrypted.length - debugBytesLength);
                receivedDataBuffer.write(rawDataBuffer, 0, rawDataBuffer.length);

                // jeśli wykryje, że klatki są w niewłaściwej kolejności, ustaw klatkę jako corrupted
                if (realPackagesIteration < packageIteration - 1) {
                    isCorrupted = true;
                }
                realPackagesIteration++;

                if (isTerminated) {
                    // poskładaj klatki i wygeneruj obraz jeśli przesłano wszystkie
                    // fragmenty klatki oraz nie są one uszkodzone
                    if (countOfPackages == packageIteration && !isCorrupted) {
                        final byte[] receivedData = receivedDataBuffer.toByteArray();
                        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedData);
                        final BufferedImage receivedImage = ImageIO.read(byteArrayInputStream);
                        final BufferedImage renderedImage = Scalr.resize(receivedImage, size.width, size.height);
                        videoCanvasController.setReceivedAndRenderedImage(receivedImage, renderedImage);
                        byteArrayInputStream.close();
                        videoCanvas.repaint();
                    }
                    if (isCorrupted) {
                        corruptedFrames++;
                    }
                    isCorrupted = false;
                    receivedDataBuffer.reset(); // wyczyść bufor na fragmenty klatek
                    realPackagesIteration = 0;
                }
            } catch (Exception ex) {
                isCorrupted = false;
                realPackagesIteration = 0;
                receivedDataBuffer.reset();
            }
            if (logTimer >= BILION * 6L) {
                if (recvBytes > 0) {
                    log.info("Client datagram socket processed {} bytes. Lost frames: {}", recvBytes, corruptedFrames);
                }
                logTimer = 0;
            }
            if (timer >= BILION) {
                clientState.updateRecvBytesPerSec(recvBytes);
                log.debug("Client datagram socket processed {} bytes", recvBytes);
                clientState.updateLostFramesCount(corruptedFrames);
                corruptedFrames = 0;
                recvBytes = 0;
                timer = 0;
            }
        }
        stopAndClear();
    }

    @Override
    public void createDatagramSocket(byte[] secretKey, int port) {
        try {
            cryptoSymmetricHelper.initDecrypt(secretKey);
            datagramSocket = new DatagramSocket(port);
            datagramSocket.setSoTimeout(1000);
        } catch (Exception ex) {
            clientState.updateConnectionState(ConnectionState.DISCONNECTED);
            throw new UnoperableException(ex);
        }
    }

    @Override
    protected void abstractStopAndClear() {
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();
        if (!visibilityState.equals(VisibilityState.TEMPORARY_HIDDEN)) {
            clientState.updateVisibilityState(VisibilityState.WAITING_FOR_CONNECTION);
        }
        clientState.updateFrameAspectRation(SharedConstants.DEFAULT_ASPECT_RATIO);
        clientState.updateRecvBytesPerSec(0L);
        bottomInfobarController.stopConnectionTimer();
    }

    @Override
    protected void initObservables() {
        clientState.wrapAsDisposable(clientState.getVisibilityState$(), visibilityState -> {
            isThreadActive = visibilityState.equals(VisibilityState.VISIBLE);
            this.visibilityState = visibilityState;
        });
    }
}
