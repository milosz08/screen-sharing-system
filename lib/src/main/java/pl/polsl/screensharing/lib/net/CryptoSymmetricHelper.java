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

@Getter
public class CryptoSymmetricHelper {
    private Cipher cipher;
    private final SecureRandom secureRandom;

    public CryptoSymmetricHelper() {
        secureRandom = new SecureRandom();
    }

    public void initEncrypt(byte[] secretKey, byte[] initVector) throws Exception {
        init(secretKey, initVector, Cipher.ENCRYPT_MODE);
    }

    public void initDecrypt(byte[] secretKey, byte[] initVector) throws Exception {
        init(secretKey, initVector, Cipher.DECRYPT_MODE);
    }

    public byte[] encrypt(byte[] rawData) throws Exception {
        return cipher.doFinal(rawData);
    }

    public byte[] decrypt(byte[] encryptedData) throws Exception {
        return cipher.doFinal(encryptedData);
    }

    private void init(byte[] secretKey, byte[] initVector, int mode) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
        // Stworzenie instancji szyfrowania AES, tryb licznikowy (CTR), aby nie było różnic w długości niekodowanych
        // i kodowanych bajtów. Bez paddingu ponieważ długość danych jest wielokrotnością długości klucza (128bit)
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(mode, secretKeySpec, ivParameterSpec);
    }
}
