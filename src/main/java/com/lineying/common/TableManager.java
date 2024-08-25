package com.lineying.common;

import java.util.Arrays;
import java.util.List;

/**
 * appcode管理
 */
public class TableManager {

    private static List<String> mAppCodeServers = Arrays.asList("mathcalc", "scancode", "linevideo", "smsmessenger");

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
            case "linevideo":
                return "_video";
            case "smsmessenger":
                return "_messenger";
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
     * 获取用户设置表
     * @param appcode
     * @return
     */
    public static String getUserSettingTable(String appcode) {
        return "user_setting" + getTableSuffix(appcode);
    }

    /**
     * 获取反馈表
     * @param appcode
     * @return
     */
    public static String getFeedbackTable(String appcode) {
        return "feedback" + getTableSuffix(appcode);
    }

    /**
     * 获取订单表
     * @param appcode
     * @return
     */
    public static String getOrderTable(String appcode) {
        return "order_gen";
    }

    /**
     * 获取商品表
     * @param appcode
     * @return
     */
    public static String getGoodsTable(String appcode) {
        return "goods" + getTableSuffix(appcode);
    }

    /**
     * 获取兑换码表
     * @param appcode
     * @return
     */
    public static String getRedeemTable(String appcode) {
        return "redeemcode_gen";
    }

    /**
     * 获取广告配置表
     * @return
     */
    public static String getAdTable() {
        return "adgen";
    }

    /**
     * 获取媒体播放计划
     * @return
     */
    public static String getMediaPlanTable() {
        return "media_plan";
    }

    /**
     * 获取媒体播放计划
     * @return
     */
    public static String getAdBrandTable() {
        return "adbrand";
    }

    /**
     * 获取版本信息表
     * @param appcode
     * @return
     */
    public static String getVersionTable(String appcode) {
        return "version" + getTableSuffix(appcode);
    }

}
