/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.CryptoUtils;
import pl.polsl.screensharing.lib.UnoperableException;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;

@Slf4j
public class ClientTcpSocket extends Thread {
    private Socket clientSocket;
    private KeyPair clientKeypair;
    private PublicKey serverPublicKey;

    private final FastConnectionDetails fastConnectionDetails;
    private final ClientDatagramSocket clientDatagramSocket;
    private final ConnectionHandler connectionHandler;
    private final ConnectionDetails connectionDetails;

    public ClientTcpSocket(
        ClientWindow clientWindow, ConnectionHandler connectionHandler, ConnectionDetails connectionDetails
    ) {
        clientDatagramSocket = clientWindow.getClientDatagramSocket();
        fastConnectionDetails = clientWindow.getClientState().getLastEmittedFastConnectionDetails();
        this.connectionHandler = connectionHandler;
        this.connectionDetails = connectionDetails;
    }

    @Override
    public void run() {
        final String ipAddress = fastConnectionDetails.getIpAddress();
        final int port = fastConnectionDetails.getPort();
        log.info("Starting TCP client thread...");
        try {
            while (clientSocket.isConnected()) {
                // wysłanie klucza publicznego do hosta, odebranie klucza publicznego od hosta
                // sprwadzenie hasła połączenia, jeśli host ustawił hasło, jeśli hasło jest git nawiąż połaczenie

                connectionHandler.onSuccess(connectionDetails);
//                connectionHandler.onFailure(connectionDetails, "123");

            }
        } catch (SecurityException ex) {
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        log.info("Disconnected with host: {}:{}", ipAddress, port);
        log.debug("Collected detatched thread with TID {} by GC", getName());
        if (clientDatagramSocket != null) {
            clientDatagramSocket.stopAndClear();
        }
        stopAndClear();
    }

    @Override
    public synchronized void start() {
        try {
            clientSocket = createSocket();
            clientKeypair = CryptoUtils.generateRsaKeypair();
            if (!isAlive()) {
                setName("Thread-TCP-" + getId());
                super.start();
            }
        } catch (IOException | GeneralSecurityException ex) {
            log.error(ex.getMessage());
            connectionHandler.onFailure(connectionDetails, null);
        }
    }

    public void stopAndClear() {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            throw new UnoperableException(ex);
        }
    }

    private Socket createSocket() throws IOException {
        final String ipAddress = fastConnectionDetails.getIpAddress();
        final int port = fastConnectionDetails.getPort();
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(ipAddress, port));
        log.info("Successfully created connection with {}:{} server", ipAddress, port);
        return socket;
    }
}
