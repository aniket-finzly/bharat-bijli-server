package com.finzly.bbc.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

class EncryptionUtil {
    private static final String SECRET_KEY = System.getenv ("SECRET_KEY"); // Manage securely through environment variable

    public static String encrypt (String data) throws Exception {
        Key key = new SecretKeySpec (SECRET_KEY.getBytes (), "AES");
        Cipher cipher = Cipher.getInstance ("AES");
        cipher.init (Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal (data.getBytes ());
        return Base64.getEncoder ().encodeToString (encryptedData);
    }

    public static String decrypt (String encryptedData) throws Exception {
        Key key = new SecretKeySpec (SECRET_KEY.getBytes (), "AES");
        Cipher cipher = Cipher.getInstance ("AES");
        cipher.init (Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal (Base64.getDecoder ().decode (encryptedData));
        return new String (decryptedData);
    }
}