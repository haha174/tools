package com.wen.tools.cryption;

import com.wen.tools.domain.utils.ParameterUtils;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESEncryptor {
    private static final ParameterUtils properties;

    private static final String DES_ALGORITHM = "DES";

    static {
        try {
            properties = ParameterUtils.fromPropertiesFile("encryptor.properties");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static final String SECRETKEY = properties.get("SECRETKEY");

    public static String encrypt(String text) {
        try {
            return encrypt(text, SECRETKEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            return decrypt(encryptedText, SECRETKEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey generateKey(String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }

    public static String encrypt(String content, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
        cipher.init(1, generateKey(secretKey));
        byte[] byte_encode = content.getBytes();
        byte[] byte_AES = cipher.doFinal(byte_encode);
        return new String((new BASE64Encoder()).encode(byte_AES));
    }

    public static String decrypt(String content, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
        cipher.init(2, generateKey(secretKey));
        byte[] byte_content = (new BASE64Decoder()).decodeBuffer(content);
        byte[] byte_decode = cipher.doFinal(byte_content);
        return new String(byte_decode);
    }

    public static void main(String[] args) throws Exception {
        System.err.println(encrypt("welcome_1911", SECRETKEY));
    }
}
