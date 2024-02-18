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
     * 获取表后缀
     * @param appcode
     * @return
     */
    private static String getTableSuffix(String appcode) {
        switch (appcode) {
            case "mathcalc":
                return "_cal";
            case "scancode":
                return "_qrcode";
        }
        return "";
    }

    /**
     * 获取用户表
     * @param appcode
     * @return
     */
    public static String getUserTable(String appcode) {
        return "user" + getTableSuffix(appcode);
    }

    /**
     * 获取反馈表
     * @param appcode
     * @return
     */
    public static String getFeedbackTable(String appcode) {
        return "feedback" + getTableSuffix(appcode);
    }

}
