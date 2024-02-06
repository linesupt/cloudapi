package com.lineying.common;

import java.util.Arrays;
import java.util.List;

/**
 * appcode管理
 */
public class AppCodeManager {

    private static List<String> mAppCodeServers = Arrays.asList("mathcalc", "scancode", "linevideo");

    /**
     * 是否包含
     * @param appcode
     * @return
     */
    public static boolean contains(String appcode) {
        return mAppCodeServers.contains(appcode);
    }

    /**
     * 获取用户表
     * @param appcode
     * @return
     */
    public static String getUserTable(String appcode) {

        switch (appcode) {
            case "mathcalc":
                return "user_cal";
            case "scancode":
                return "user_qrcode";
        }
        return "";
    }

}
