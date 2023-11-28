/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Getter
public class CryptoSymmetricHelper {
    private Cipher cipher;
    private final SecureRandom secureRandom;

    public CryptoSymmetricHelper() {
        secureRandom = new SecureRandom();
    }

    public void initEncrypt(byte[] secretKey) throws Exception {
        init(secretKey, Cipher.ENCRYPT_MODE);
    }

    public void initDecrypt(byte[] secretKey) throws Exception {
        init(secretKey, Cipher.DECRYPT_MODE);
    }

    public byte[] encrypt(byte[] rawData) throws Exception {
        return cipher.doFinal(rawData);
    }

    public byte[] decrypt(byte[] encryptedFrameData) throws Exception {
        return cipher.doFinal(encryptedFrameData);
    }

    private void init(byte[] secretKey, int mode) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(mode, secretKeySpec);
    }
}
