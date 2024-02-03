package com.lineying.util;

import java.security.SecureRandom;

public class RandomUtil {

    private static final char[] SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final SecureRandom random = new SecureRandom();

    private RandomUtil() {
    }

    private static String createNonce(int length) {
        char[] buf = new char[length];

        for(int i = 0; i < length; ++i) {
            buf[i] = SYMBOLS[random.nextInt(SYMBOLS.length)];
        }

        return new String(buf);
    }

    /**
     * 生成一个随机名
     * @return
     */
    public static String makeName() {
        String random = createNonce(14);
        return "usr_" + random.toLowerCase();
    }

}
