package com.lineying.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lance on 16/7/18.
 * 时间/时长装换工具
 */
public class TimeUtil {

    // 仅仅格式化年月日
    static final String DATE_FORMAT = "yyyy-MM-dd";
    // 时刻格式化
    static final String TIME_FORMAT = "HH:mm:ss";
    // 全程格式化
    static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DATETIME_FORMAT_ORDER = "yyyyMMddHHmmssSSS";

    /**
     * 自定义格式化日期格式
     * @param date
     * @param formatp
     * @return
     */
    public static String formatTime(Date date, String formatp) {
        SimpleDateFormat format = new SimpleDateFormat(formatp);

        return format.format(date);
    }

    /**
     * 自定义日期格式化
     * @param millisSeconds
     * @param formatp
     * @return
     */
    public static String formatTime(long millisSeconds, String formatp) {
        SimpleDateFormat format = new SimpleDateFormat(formatp);

        return format.format(new Date(millisSeconds));
    }

    /**
     * 格式化为日期时间
     * @param date
     * @return
     */
    public static String datetime(Date date) {

        return formatTime(date, DATETIME_FORMAT);
    }

    public static String datetimeOrder(Date date) {

        return formatTime(date, DATETIME_FORMAT_ORDER);
    }

    /**
     * 仅仅格式化为日期
     * @param date
     * @return
     */
    public static String date(Date date) {

        return formatTime(date, DATE_FORMAT);
    }

    /**
     * 显示年月日及时间格式
     * @param millisSeconds 1970-now millis
     * @return
     */
    public static String datetime(long millisSeconds) {

        return datetime(new Date(millisSeconds));
    }

    /**
     * 日期时间订单
     * @param millisSeconds
     * @return
     */
    public static String datetimeOrder(long millisSeconds) {

        return datetimeOrder(new Date(millisSeconds));
    }

    /**
     * 显示年月日格式
     * @param millisSeconds 1970-now millis
     * @return
     */
    public static String date(long millisSeconds) {

        return date(new Date(millisSeconds));
    }

    /**
     * 精确到毫秒的时长
     * @param millisSeconds
     * @return
     */
    public static String durationMillis(long millisSeconds) {

        return duration(millisSeconds, true);
    }

    /**
     * 将格式化为时长格式
     * @param millisSeconds 时长的毫秒值
     * @param accurateEnabled 是否精确到毫秒
     * @return
     */
    public static String duration(long millisSeconds, boolean accurateEnabled) {

        // 小时
        long hour = millisSeconds / (1000 * 3600);
        // 取小时后余下的分 毫秒值
        long minuteMillis = millisSeconds % (1000 * 3600);
        // 得到分
        long minute = minuteMillis / (1000 * 60);

        // 取分后余下的毫秒值
        long secondMillis = minuteMillis % (1000 * 60);

        long second = secondMillis / 1000;

        // 毫秒 只取1位数即可
        long millisSecond = secondMillis % 1000 / 100;

        String formatStr = "%02d:%02d:%02d.%01d";
        String result = "--:--.-";
        if (millisSeconds < 60 * 60 * 1000) {// 小于1小时的情况
            if (accurateEnabled) {
                formatStr = "%02d:%02d.%01d";
                result = String.format(formatStr, minute, second, millisSecond);
            } else {
                formatStr = "%02d:%02d";
                result = String.format(formatStr, minute, second);
            }

        } else {// 最低要显示分
            if (accurateEnabled) {
                formatStr = "%02d:%02d:%02d.%01d";
                result = String.format(formatStr, hour, minute, second, millisSecond);
            } else {
                formatStr = "%02d:%02d:%02d";
                result = String.format(formatStr, hour, minute, second);
            }

        }

        return result;
    }

}
