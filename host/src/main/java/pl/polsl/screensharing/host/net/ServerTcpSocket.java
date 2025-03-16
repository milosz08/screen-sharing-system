package pl.polsl.screensharing.host.net;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.controller.BottomInfobarController;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.net.AbstractTcpSocketThread;
import pl.polsl.screensharing.lib.net.SocketState;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class ServerTcpSocket extends AbstractTcpSocketThread<ServerSocket> {
    private boolean isEstabilished;
    private ConcurrentMap<Long, ConnectedClientInfo> connectedClients;

    @Getter
    private final HostWindow hostWindow;
    private final HostState hostState;
    private final ServerDatagramSocket serverDatagramSocket;
    @Getter
    private final SessionDetails sessionDetails;
    private final ConnectionHandler connectionHandler;

    public ServerTcpSocket(HostWindow hostWindow, ConnectionHandler connectionHandler) {
        super();
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
                final Socket clientSocket = socket.accept();
                final ClientThread clientThread = new ClientThread(clientSocket, this);
                clientThread.start();
            }
        } catch (SocketException ignored) {
            stopAndClear();
        } catch (IOException ex) {
            stopAndClear();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    @Override
    public synchronized void startExecutor() {
        try {
            initSocketAndKeys();
            isEstabilished = true;
            startThread();
            connectionHandler.onSuccess();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            connectionHandler.onFailure(ex);
        }
    }

    @Override
    protected void createTcpSocket() throws IOException {
        final String ipAddress = sessionDetails.getIpAddress();
        final int port = sessionDetails.getPort();
        socket = new ServerSocket();
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(ipAddress, port));
        log.info("TCP server created with details: {}", sessionDetails);
    }

    @Override
    protected void abstractStopAndClear() {
        if (serverDatagramSocket != null) {
            serverDatagramSocket.stopAndClear();
        }
        if (!socket.isClosed()) {
            closeSocket();
        }
        final BottomInfobarController bottomInfobarController = hostWindow.getBottomInfobarController();
        bottomInfobarController.stopSessionTimer();
        hostState.updateSessionState(SessionState.INACTIVE);
        hostState.updateSentBytesPerSec(0L);
        isEstabilished = false;
        sendSignalToAllClients(SocketState.END_UP_SESSION);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), connectedClients -> {
            this.connectedClients = connectedClients;
        });
    }
}
