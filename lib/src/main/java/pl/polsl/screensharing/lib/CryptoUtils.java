/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoUtils {
    public static KeyPair generateRsaKeypair() throws GeneralSecurityException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        log.info("Successfully generated public and private RSA keys");
        return keyPair;
    }

    public static PublicKey base64ToPublicKey(String base64Key) throws Exception {
        final byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String rsaAysmEncrypt(String data, PublicKey publicKey) {
        byte[] encrypted;
        try {
            final Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = encryptCipher.doFinal(data.getBytes());
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String rsaAsymDecrypt(String data, PrivateKey privateKey) {
        String decrypted;
        try {
            final Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            decrypted = new String(decryptCipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
        return decrypted;
    }

    public static Cipher initEncryptSymAes(byte[] secretKey, byte[] initVector) {
        return initSymAes(secretKey, initVector, Cipher.ENCRYPT_MODE);
    }

    public static Cipher initDecryptSymAes(byte[] secretKey, byte[] initVector) {
        return initSymAes(secretKey, initVector, Cipher.DECRYPT_MODE);
    }

    public static Cipher initSymAes(byte[] secretKey, byte[] initVector, int mode) {
        Cipher cipher;
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(mode, secretKeySpec, ivParameterSpec);
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
        return cipher;
    }
}
