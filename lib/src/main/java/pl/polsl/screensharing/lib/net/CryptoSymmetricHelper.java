/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static pl.polsl.screensharing.lib.SharedConstants.IV_SIZE;
import static pl.polsl.screensharing.lib.SharedConstants.PACKAGE_SIZE;

@Getter
public class CryptoSymmetricHelper {
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;
    private final SecureRandom secureRandom;

    public CryptoSymmetricHelper() {
        secureRandom = new SecureRandom();
    }

    public byte[] encrypt(byte[] rawData) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, secureRandom);
        final byte[] iv = cipher.getIV();
        final byte[] encrypted = cipher.doFinal(rawData);
        // dodanie IV wygenerowanego losowo do zaszyfrowanych danych
        System.arraycopy(iv, 0, encrypted, encrypted.length - iv.length, iv.length);
        return encrypted;
    }

    public byte[] decrypt(byte[] data, int length) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        byte[] encryptedWithoutIv = new byte[PACKAGE_SIZE];
        // przenoszenie IV do tablicy iv
        System.arraycopy(data, length - iv.length, iv, 0, iv.length);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        // odszyfrowanie z przesunięciem uwzględniającym wektor IV
        cipher.doFinal(data, 0, length - iv.length, encryptedWithoutIv);
        return encryptedWithoutIv;
    }

    public void init(byte[] secretKey) throws Exception {
        secretKeySpec = new SecretKeySpec(secretKey, "AES");
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
    }
}
