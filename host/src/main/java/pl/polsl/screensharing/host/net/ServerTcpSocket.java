/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.CryptoUtils;
import pl.polsl.screensharing.lib.UnoperableException;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyPair;

@Slf4j
public class ServerTcpSocket extends Thread {
    private ServerSocket serverSocket;
    private boolean isEstabilished;
    private KeyPair serverKeypair;

    private final HostWindow hostWindow;
    private final ServerDatagramSocket serverDatagramSocket;
    private final SessionDetails sessionDetails;
    private final ConnectionHandler connectionHandler;

    public ServerTcpSocket(HostWindow hostWindow, ConnectionHandler connectionHandler) {
        this.hostWindow = hostWindow;
        serverDatagramSocket = hostWindow.getServerDatagramSocket();
        sessionDetails = hostWindow.getHostState().getLastEmittedSessionDetails();
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void run() {
        log.info("Starting TCP server thread...");
        try {
            while (isEstabilished) {
                final Socket clientSocket = serverSocket.accept();
                final ClientThread clientThread = new ClientThread(clientSocket, hostWindow, sessionDetails,
                    serverKeypair);
                clientThread.start();
            }
        } catch (SocketException ignored) {
        } catch (IOException ex) {
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        log.info("Stopping TCP server thread with TID {}", getName());
        log.debug("Collected detatched thread with TID {} by GC", getName());
        if (serverDatagramSocket != null) {
            serverDatagramSocket.stopAndClear();
        }
        isEstabilished = false;
        if (!serverSocket.isClosed()) {
            closeSocket();
        }
    }

    @Override
    public synchronized void start() {
        try {
            createServerSocket();
            serverKeypair = CryptoUtils.generateRsaKeypair();
            isEstabilished = true;
            if (!isAlive()) {
                setName("Thread-TCP-" + getId());
                super.start();
            }
            connectionHandler.onSuccess();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            connectionHandler.onFailure(ex);
        }
    }

    public void stopAndClear() {
        closeSocket();
        isEstabilished = false;
    }

    private void closeSocket() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            throw new UnoperableException(ex);
        }
    }

    private void createServerSocket() throws IOException {
        final String ipAddress = sessionDetails.getIpAddress();
        final int port = sessionDetails.getPort();
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(ipAddress, port));
        log.info("TCP server created with details: {}", sessionDetails);
    }
}
