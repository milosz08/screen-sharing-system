package pl.polsl.screensharing.host.net;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.lib.net.CryptoSymmetricHelper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class FrameSenderThread extends Thread {
    private final ServerDatagramSocket serverDatagramSocket;
    private final HostState hostState;
    private final CryptoSymmetricHelper cryptoSymmetricHelper;

    private ConcurrentMap<Long, ConnectedClientInfo> connectedClients;
    private final BlockingQueue<byte[]> sendPackagesQueue;

    public FrameSenderThread(ServerDatagramSocket serverDatagramSocket) {
        this.serverDatagramSocket = serverDatagramSocket;
        hostState = serverDatagramSocket.getHostState();
        cryptoSymmetricHelper = serverDatagramSocket.getCryptoSymmetricHelper();
        connectedClients = new ConcurrentHashMap<>();
        sendPackagesQueue = serverDatagramSocket.getSendPackagesQueue();
        initObservables();
    }

    @Override
    public void run() {
        log.info("Started datagram frame sender thread with TID {}", getName());
        while (serverDatagramSocket.isThreadActive()) {
            try {
                final DatagramSocket datagramSocket = serverDatagramSocket.getDatagramSocket();
                final byte[] rawChunk = sendPackagesQueue.take();
                final byte[] encryptedChunk = cryptoSymmetricHelper.encrypt(rawChunk);
                for (final Map.Entry<Long, ConnectedClientInfo> clientEntry : connectedClients.entrySet()) {
                    final ConnectedClientInfo client = clientEntry.getValue();
                    final DatagramPacket packet = new DatagramPacket(encryptedChunk, encryptedChunk.length,
                        InetAddress.getByName(client.getIpAddress()), client.getUdpPort());
                    datagramSocket.send(packet);
                }
            } catch (Exception ignored) {
            }
        }
        log.info("Stopping frame sender thread with TID {}", getName());
        log.debug("Collected frame sender thread with TID {} by GC", getName());
    }

    @Override
    public synchronized void start() {
        if (!isAlive()) {
            setName("Thread-UDP-Frame-" + getId());
            super.start();
        }
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), connectedClients -> {
            this.connectedClients = new ConcurrentHashMap<>(connectedClients);
        });
    }
}
