/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.RequiredArgsConstructor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@RequiredArgsConstructor
public class DatagramFrameThread extends Thread {
    private final byte[] chunk;
    private final DatagramSocket datagramSocket;
    private final ConnectedClientInfo connectedClientInfo;

    @Override
    public void run() {
        try {
            final DatagramPacket packet = new DatagramPacket(chunk, chunk.length,
                InetAddress.getByName(connectedClientInfo.getIpAddress()), connectedClientInfo.getUdpPort());
            datagramSocket.send(packet);
        } catch (Exception ignored) {
        }
    }
}
