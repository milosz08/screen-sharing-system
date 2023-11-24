/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.state.HostState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
public class ClientThread extends Thread {
    private final Socket socket;
    private final HostState hostState;
    private final SessionDetails sessionDetails;
    private final ConnectionHandler connectionHandler;
    private final KeyPair serverKeyPair;

    private PublicKey clientPublicKey;

    @Override
    public void run() {
        log.info("Started client thread with PID {}", getId());
        try (
            final InputStreamReader rd = new InputStreamReader(socket.getInputStream());
            final BufferedReader in = new BufferedReader(rd);
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            while (socket.isConnected()) {
                if (in.readLine() == null) {
                    break;
                }
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        log.info("Stopping TCP client thread");
        log.debug("Collected detatched thread with TID {} by GC", getName());
        final ConcurrentMap<Long, ConnectedClientInfo> allClients = hostState.getLastEmittedConnectedClients();
        final ConnectedClientInfo removed = allClients.remove(getId());
        hostState.updateConnectedClients(allClients);
        log.info("Removed user with details {} from all connected users list", removed);
        try {
            socket.close();
        } catch (IOException ignore) {
        }
    }

    @Override
    public synchronized void start() {
        setName("Thread-TCP-Client-" + getId());
        if (!isAlive()) {
            super.start();
        }
    }
}
