package org.asuki.common.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class Cryptos {

    private Cryptos() {
    }

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final Key KEY = new SecretKeySpec(
            "MySuperSecretKey".getBytes(), "AES");

    public static String encrypt(String plainText) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, KEY);

            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, KEY);

            return new String(cipher.doFinal(Base64.getDecoder().decode(
                    cipherText)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
