package com.finzly.bbc.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] IV = new byte[16];

    static {
        SecureRandom random = new SecureRandom ();
        random.nextBytes (IV);
    }

    private static SecretKey getSecretKey (String secret) {
        return new SecretKeySpec (secret.getBytes (), ALGORITHM);
    }

    public static String encrypt (String data, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance (TRANSFORMATION);
        SecretKey key = getSecretKey (secret);
        cipher.init (Cipher.ENCRYPT_MODE, key, new IvParameterSpec (IV));
        byte[] encryptedData = cipher.doFinal (data.getBytes ());
        return Base64.getEncoder ().encodeToString (encryptedData) + ":" + Base64.getEncoder ().encodeToString (IV);
    }

    public static String decrypt (String encryptedData, String secret) throws Exception {
        String[] parts = encryptedData.split (":");
        byte[] iv = Base64.getDecoder ().decode (parts[1]);
        byte[] encryptedBytes = Base64.getDecoder ().decode (parts[0]);

        Cipher cipher = Cipher.getInstance (TRANSFORMATION);
        SecretKey key = getSecretKey (secret);
        cipher.init (Cipher.DECRYPT_MODE, key, new IvParameterSpec (iv));
        byte[] decryptedData = cipher.doFinal (encryptedBytes);
        return new String (decryptedData);
    }
}
