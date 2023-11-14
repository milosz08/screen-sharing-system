/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StartNet implements Runnable {

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    @Override
    public void run() {
        try (final DatagramSocket clientSocket = new DatagramSocket(7648)) {
            System.out.println("start listening data");
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                byte[] encryptedData = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());

                byte[] decryptedData = decrypt(encryptedData);

                String message = new String(encryptedData);
                System.out.println("Received and decrypted message: " + message);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static byte[] decrypt(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(encryptedData);
    }

}
