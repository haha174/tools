package com.wen.tools.cryption;

import nyla.solutions.global.util.Cryption;

public class CryptionUtil {
    public static String getEncryptionString(String str) {
        Cryption cryption = new Cryption();
        try {
            return Cryption.CRYPTION_PREFIX + cryption.encryptText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDecryptionString(String str, boolean flag) {
        if (flag == true) {
            str = Cryption.CRYPTION_PREFIX + str;
        }
        return Cryption.interpret(str);

    }
}
