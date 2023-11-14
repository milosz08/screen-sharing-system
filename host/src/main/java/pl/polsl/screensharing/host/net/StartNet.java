/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class StartNet implements Runnable {

    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    @Override
    public void run() {
        try (final DatagramSocket serverSocket = new DatagramSocket()) {

            while (true) {

                String message = "Hello, Server!";
                byte[] rawData = message.getBytes();

                byte[] encryptedData = encrypt(rawData);

                InetAddress serverAddress = InetAddress.getByName("localhost");
                int serverPort = 7648;
                DatagramPacket sendPacket = new DatagramPacket(encryptedData, encryptedData.length, serverAddress, serverPort);

                serverSocket.send(sendPacket);


                Thread.sleep(2000);
            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static byte[] encrypt(byte[] rawData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(rawData);
    }

}
