/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.UnoperableException;

import java.io.Closeable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;

@Slf4j
public abstract class AbstractTcpSocketThread<S extends Closeable> extends Thread {
    @Getter
    protected S socket;
    @Getter
    protected CryptoAsymmetricHelper cryptoAsymmetricHelper;
    protected PublicKey exchangePublicKey;

    protected AbstractTcpSocketThread() {
        cryptoAsymmetricHelper = new CryptoAsymmetricHelper();
    }

    protected void initSocketAndKeys() throws IOException, GeneralSecurityException {
        createTcpSocket();
        cryptoAsymmetricHelper.generateRsaKeypair();
    }

    protected synchronized final void startThread() {
        if (!isAlive()) {
            setName("Thread-TCP-" + getId());
            start();
        }
    }

    public final void stopAndClear() {
        abstractStopAndClear();
        log.info("Stopping TCP thread: {}", getName());
        log.debug("Collected detatched thread with TID {} by GC", getName());
        closeSocket();
    }

    protected void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            throw new UnoperableException(ex);
        }
    }

    protected abstract void createTcpSocket() throws IOException;
    protected abstract void startExecutor();
    protected abstract void abstractStopAndClear();
}
