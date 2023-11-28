/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramSocket;

@Slf4j
@Getter
public abstract class AbstractDatagramSocketThread extends Thread {
    protected DatagramSocket datagramSocket;
    protected CryptoSymmetricHelper cryptoSymmetricHelper;
    protected boolean isThreadActive;

    protected AbstractDatagramSocketThread() {
        cryptoSymmetricHelper = new CryptoSymmetricHelper();
    }

    public final void stopAndClear() {
        isThreadActive = false;
        log.info("Stopping datagram thread with TID {}", getName());
        log.debug("Collected detatched thread with TID {} by GC", getName());
        datagramSocket.disconnect();
        datagramSocket.close();
        abstractStopAndClear();
    }

    @Override
    public final synchronized void start() {
        isThreadActive = true;
        if (!isAlive()) {
            setName("Thread-UDP-" + getId());
            super.start();
            postStart();
        }
    }

    protected void postStart() {
    }

    public abstract void createDatagramSocket(byte[] secretKey, int port);
    protected abstract void abstractStopAndClear();
    protected abstract void initObservables();
}
