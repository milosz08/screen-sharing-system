package pl.polsl.screensharing.host.net;

import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.UnoperableException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class DatagramKey {
    private final SecureRandom secureRnd;

    private SecretKey secretKey;

    public DatagramKey() {
        secureRnd = new SecureRandom();
    }

    public void generateKey() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(SharedConstants.AES_KEY_SIZE, secureRnd);
            secretKey = keyGenerator.generateKey();
        } catch (Exception ex) {
            throw new UnoperableException(ex);
        }
    }

    public byte[] getSecretKey() {
        return secretKey.getEncoded();
    }
}
