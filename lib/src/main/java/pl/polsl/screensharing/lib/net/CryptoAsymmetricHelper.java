/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Getter
public class CryptoAsymmetricHelper {
    private KeyPair keyPair;

    public void generateRsaKeypair() throws GeneralSecurityException {
        // Generacja zestawu kluczy publiczny, prywatny typu RSA o długości 2048 bitów.
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
        log.info("Successfully generated public and private RSA keys");
    }

    public PublicKey base64ToPublicKey(String base64Key) throws Exception {
        // Konwersja klucza przesyłanego przez sieć z Base64 do tablicy bajtów
        // oraz na jej podstawie zainicjalizowanie instancji PublicKey
        final byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public String publicKeyToBase64() {
        // Konwersja klucza publicznego do formatu Base64 aby można go było przesłać przez sieć.
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String encrypt(String data, PublicKey publicKey) throws Exception {
        // Szyfrowanie danych przychodzących kluczem publiczny oraz zakodowanie w Base64
        final Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(encryptCipher.doFinal(data.getBytes()));
    }

    public String decrypt(String data) throws Exception {
        // Deszyfrowanie danych przychodzących kluczem prywatnym oraz odkodowanie z Base64
        final Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        return new String(decryptCipher.doFinal(Base64.getDecoder().decode(data)));
    }
}
