package com.finzly.bbc.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // Consistent IV, decoded from base64 (16 bytes for AES CBC)
    private static final byte[] IV = Base64.getDecoder ().decode ("MTIzNDU2Nzg5MDEyMzQ1Ng==");

    // Generate the AES secret key from the provided secret
    private static SecretKey getSecretKey (String secret) {
        return new SecretKeySpec (secret.getBytes (), ALGORITHM);
    }

    // Encrypts the data using AES and the provided secret key
    public static String encrypt (String data, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance (TRANSFORMATION);
        SecretKey key = getSecretKey (secret);
        cipher.init (Cipher.ENCRYPT_MODE, key, new IvParameterSpec (IV));
        byte[] encryptedData = cipher.doFinal (data.getBytes ());
        return Base64.getEncoder ().encodeToString (encryptedData); // Don't include IV again as it's constant
    }

    // Decrypts the data using AES and the provided secret key
    public static String decrypt (String encryptedData, String secret) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder ().decode (encryptedData);

        Cipher cipher = Cipher.getInstance (TRANSFORMATION);
        SecretKey key = getSecretKey (secret);
        cipher.init (Cipher.DECRYPT_MODE, key, new IvParameterSpec (IV));
        byte[] decryptedData = cipher.doFinal (encryptedBytes);
        return new String (decryptedData);
    }
}
