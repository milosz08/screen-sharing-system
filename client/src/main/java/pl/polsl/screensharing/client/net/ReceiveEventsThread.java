/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
@RequiredArgsConstructor
public class ReceiveEventsThread extends Thread {
    private final ClientTcpSocket clientTcpSocket;
    private final Socket socket;

    public ReceiveEventsThread(ClientTcpSocket clientTcpSocket) {
        this.clientTcpSocket = clientTcpSocket;
        socket = clientTcpSocket.getClientSocket();
    }

    @Override
    public void run() {
        log.info("Starting TCP client receive events thread...");
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                // TODO: event loop for receiving host events
                System.out.println("Event action: " + line);
            }
        } catch (SocketException ex) {
            clientTcpSocket.stopAndClear();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public synchronized void start() {
        if (!isAlive()) {
            setName("Thread-TCP-Event-Recv-" + getId());
            super.start();
        }
    }
}
