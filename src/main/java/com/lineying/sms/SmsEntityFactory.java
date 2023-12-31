package com.lineying.sms;

/**
 * 短信API相关
 */
public class SmsEntityFactory {

    /**
     * 创建短信实体
     * @param appcode
     * @return
     */
    public static SmsEntity make(String appcode) {
        if ("mathcalc".equals(appcode)) {
            return new SmsEntity("mathcalc", "1400875159", "千维计算器", "2014739");
        } else if ("scancode".equals(appcode)) {
            return new SmsEntity("scancode", "1400875160", "简码App", "2014740");
        }

        return null;
    }

}
