package com.lineying.util

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by lance on 16/7/18.
 * 时间/时长装换工具
 */
object TimeUtil {

    // 仅仅格式化年月日
    val DATE_FORMAT = "yyyy-MM-dd"
    // 时刻格式化
    val TIME_FORMAT = "HH:mm:ss"
    // 全程格式化
    val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    val DATETIME_FORMAT_ORDER = "yyyyMMddHHmmssSSS"

    /**
     * 自定义格式化日期格式
     * @param date
     * @param formatp
     * @return
     */
    fun formatTime(date: Date, formatp: String): String {
        val format = SimpleDateFormat(formatp)

        return format.format(date)
    }

    /**
     * 自定义日期格式化
     * @param millisSeconds
     * @param formatp
     * @return
     */
    fun formatTime(millisSeconds: Long, formatp: String): String {
        val format = SimpleDateFormat(formatp)

        return format.format(Date(millisSeconds))
    }

    /**
     * 格式化为日期时间
     * @param date
     * @return
     */
    fun datetime(date: Date): String {

        return formatTime(date, DATETIME_FORMAT)
    }

    fun datetimeOrder(date: Date): String {

        return formatTime(date, DATETIME_FORMAT_ORDER)
    }

    /**
     * 仅仅格式化为日期
     * @param date
     * @return
     */
    fun date(date: Date): String {

        return formatTime(date, DATE_FORMAT)
    }

    /**
     * 显示年月日及时间格式
     * @param millisSeconds 1970-now millis
     * @return
     */
    fun datetime(millisSeconds: Long): String {

        return datetime(Date(millisSeconds))
    }

    fun datetimeOrder(millisSeconds: Long): String {

        return datetimeOrder(Date(millisSeconds))
    }

    /**
     * 显示年月日格式
     * @param millisSeconds 1970-now millis
     * @return
     */
    fun date(millisSeconds: Long): String {

        return date(Date(millisSeconds))
    }

    /**
     * 精确到毫秒的时长
     * @param millisSeconds
     * @return
     */
    fun durationMillis(millisSeconds: Long): String {

        return duration(millisSeconds, true)
    }

    /**
     * 将格式化为时长格式
     * @param millisSeconds 时长的毫秒值
     * @param accurateEnabled 是否精确到毫秒
     * @return
     */
    fun duration(millisSeconds: Long, accurateEnabled: Boolean): String {

        // 小时
        val hour = millisSeconds / (1000 * 3600)
        // 取小时后余下的分 毫秒值
        val minuteMillis = millisSeconds % (1000 * 3600)
        // 得到分
        val minute = minuteMillis / (1000 * 60)

        // 取分后余下的毫秒值
        val secondMillis = minuteMillis % (1000 * 60)

        val second = secondMillis / 1000

        // 毫秒 只取1位数即可
        val millisSecond = secondMillis % 1000 / 100

        var formatStr = "%02d:%02d:%02d.%01d"
        var result = "--:--.-"
        if (millisSeconds < 60 * 60 * 1000) {// 小于1小时的情况
            if (accurateEnabled) {
                formatStr = "%02d:%02d.%01d"
                result = String.format(formatStr, minute, second, millisSecond)
            } else {
                formatStr = "%02d:%02d"
                result = String.format(formatStr, minute, second)
            }

        } else {// 最低要显示分
            if (accurateEnabled) {
                formatStr = "%02d:%02d:%02d.%01d"
                result = String.format(formatStr, hour, minute, second, millisSecond)
            } else {
                formatStr = "%02d:%02d:%02d"
                result = String.format(formatStr, hour, minute, second)
            }

        }

        return result
    }

}
