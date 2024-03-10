package com.lineying.common;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleManager {

    // 简体中文
    private static List<String> zhCNs = Arrays.asList("zh-CN", "zh_CN", "zh-Hans");
    // 繁体中文
    private static List<String> zhHants = Arrays.asList("zh-TW", "zh_TW", "zh-Hant");

    /**
     * 获取语言类环境
     * @param locale
     * @return
     */
    public static Locale getLocale(String locale) {
        if (zhCNs.contains(locale)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (zhHants.contains(locale)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return Locale.ENGLISH;
    }

    /**
     * 获取商品本地化语言字符串
     * @param locale
     * @return
     */
    public static String getGoodsLocale(String locale) {
        Locale mLocale = getLocale(locale);
        if (mLocale == Locale.SIMPLIFIED_CHINESE) {
            return "zh-CN";
        }
        return "en";
    }

}
