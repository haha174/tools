package com.wen.tools.cryption;


import com.wen.tools.domain.utils.PropertiesUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Properties;

@SuppressWarnings("restriction")
public class DESEncryptor {
  private final static Properties properties=new PropertiesUtil().getProperties("encryptor.properties");
  private final static String DES = properties.getProperty("DES");
  private final static String SECRETKEY =  properties.getProperty("SECRETKEY");
  //"LJOjdffjds*9jodfw3792jJJJNldsafasdfnsfeQEJOFdfcxdfewEOJRJJ=%&*25Bl";

  /**
   * 加密
   *
   * @param text
   * @return
   */
  public static String encrypt(String text) {
    return encrypt(text, SECRETKEY);
  }

  /**
   * 解密
   *
   * @param encryptedText
   * @return
   */
  public static String decrypt(String encryptedText) {
    return decrypt(encryptedText, SECRETKEY);
  }

  public static String encrypt(String text, String key) {
    try {
      byte[] input = encrypt(text.getBytes(), key.getBytes());
      String secret = new BASE64Encoder().encode(input);
      return secret;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return text;
  }

  public static String decrypt(String text, String key) {
    if (text == null)
      return null;
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      byte[] input = decoder.decodeBuffer(text);
      byte[] secret = decrypt(input, key.getBytes());
      return new String(secret);
    } catch (Exception e) {
      e.printStackTrace();

    }
    return text;
  }

  private static byte[] encrypt(byte[] text, byte[] key) throws Exception {
    SecureRandom random = new SecureRandom();
    DESKeySpec spec = new DESKeySpec(key);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey secretKey = secretKeyFactory.generateSecret(spec);
    Cipher cipher = Cipher.getInstance(DES);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
    return cipher.doFinal(text);
  }

  private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
    SecureRandom random = new SecureRandom();
    DESKeySpec spec = new DESKeySpec(key);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey secretKey = secretKeyFactory.generateSecret(spec);
    Cipher cipher = Cipher.getInstance(DES);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
    return cipher.doFinal(data);
  }

  public static void main(String[] args) throws Exception {
//    String text = "1qaz@WSX4";
//    if(args.length>0)text = args[0];
//    System.err.println(encrypt(text, SECRETKEY));
//    System.err.println(decrypt(encrypt(text, SECRETKEY), SECRETKEY));
//
//    String psw = "KGpeDNizItkA8SM4q+C7yA==";
//    System.out.println(decrypt(psw, SECRETKEY));
//    System.err.println(encrypt("Thinkpad3", SECRETKEY));
    System.err.println(encrypt("welcome_1911", SECRETKEY));

    //  System.out.println(decrypt("welcome_1911"));
  }
}
