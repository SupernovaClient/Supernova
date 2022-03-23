package com.github.supernova.util.client;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class SecurityUtil {
    public static String getHWID(String username) {
        try{
            String toEncrypt =  username.toLowerCase() + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();
            byte byteData[] = md.digest();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public static String hash(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = md.digest(string.getBytes(StandardCharsets.UTF_8));
            String newData = hashToString(data)+"youSuckIfYouSeeThis";
            data = md.digest(newData.getBytes(StandardCharsets.UTF_8));
            return hashToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String hashToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte data : hash) {
            String hex = Integer.toHexString(0xff & data);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    private String encryptionKey;

    public SecurityUtil(String encryptionKey) {
        this.encryptionKey = encryptionKey;

        if (encryptionKey.length() != 16) {
            this.encryptionKey = "skidwareENCRYPT0";
        }
    }

    private byte[] getEncryptionKey() {
        return encryptionKey.getBytes(StandardCharsets.UTF_8);
    }

    public String encrypt(byte[] toEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = new SecretKeySpec("skidwareENCRYPT0".getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return encode(cipher.doFinal(toEncrypt));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String decrypt(String toDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = new SecretKeySpec("skidwareENCRYPT0".getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(decode(toDecrypt)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
