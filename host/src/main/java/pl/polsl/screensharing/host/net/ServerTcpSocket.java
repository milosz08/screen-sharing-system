/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.CryptoUtils;
import pl.polsl.screensharing.lib.UnoperableException;
import pl.polsl.screensharing.lib.net.SocketState;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyPair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class ServerTcpSocket extends Thread {
    private ServerSocket serverSocket;
    private boolean isEstabilished;
    private KeyPair serverKeypair;
    private ConcurrentMap<Long, ConnectedClientInfo> connectedClients;

    private final HostWindow hostWindow;
    private final HostState hostState;
    private final ServerDatagramSocket serverDatagramSocket;
    private final SessionDetails sessionDetails;
    private final ConnectionHandler connectionHandler;

    public ServerTcpSocket(HostWindow hostWindow, ConnectionHandler connectionHandler) {
        this.hostWindow = hostWindow;
        hostState = hostWindow.getHostState();
        serverDatagramSocket = hostWindow.getServerDatagramSocket();
        sessionDetails = hostWindow.getHostState().getLastEmittedSessionDetails();
        this.connectionHandler = connectionHandler;
        connectedClients = new ConcurrentHashMap<>();
        initObservables();
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
        stopAndClear();
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
        if (!serverSocket.isClosed()) {
            closeSocket();
        }
        hostState.updateSessionState(SessionState.INACTIVE);
        isEstabilished = false;
    }

    public void sendSignalToAllClients(SocketState socketState) {
        for (final Map.Entry<Long, ConnectedClientInfo> connectedClientInfoEntry : connectedClients.entrySet()) {
            final ClientThread clientThread = connectedClientInfoEntry.getValue().getClientThread();
            if (clientThread != null) {
                clientThread.sendSignalEvent(socketState);
            }
        }
    }

    public void sendSignalToClient(SocketState socketState, Long threadId) {
        final ConnectedClientInfo connectedClientInfo = connectedClients.get(threadId);
        connectedClientInfo.getClientThread().sendSignalEvent(socketState);
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

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), connectedClients -> {
            this.connectedClients = connectedClients;
        });
    }
}
