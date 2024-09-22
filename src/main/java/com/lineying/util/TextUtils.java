package com.lineying.util;

/**
 * 文本工具
 */
public class TextUtils {

    /**
     * 判断文本是否为空
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        if (text == null) {
            return true;
        }
        if (text.isEmpty()) {
            return true;
        }
        return false;
    }

}
