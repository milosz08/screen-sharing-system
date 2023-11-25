/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import pl.polsl.screensharing.lib.UnoperableException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class DatagramKeys {
    private final SecureRandom secureRnd;

    private SecretKey secretKey;
    private SecretKey secureRandom;

    public DatagramKeys() {
        secureRnd = new SecureRandom();
    }

    public void generateKeys() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, secureRnd);
            secretKey = keyGenerator.generateKey();
            secureRandom = keyGenerator.generateKey();
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
    }

    public byte[] getSecretKey() {
        return secretKey.getEncoded();
    }

    public byte[] getSecureRandom() {
        return secureRandom.getEncoded();
    }
}
