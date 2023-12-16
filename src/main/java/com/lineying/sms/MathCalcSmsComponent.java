package com.lineying.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MathCalcSmsComponent extends BaseSmsComponent {

    @Value("${sms.mathcalc.sdkAppId}")   // 注入参数值
    private String sdkAppId;

    @Value("${sms.mathcalc.secretId}")
    private String secretId;

    @Value("${sms.mathcalc.secretKey}")
    private String secretKey;

    @Value("${sms.mathcalc.signName}")
    private String signName;

    @Value("${sms.mathcalc.templateCodeId}")
    private String templateCodeId;

    @Override
    public int sendCode(String phone, String code) {
        return sendCode(sdkAppId, signName, phone, code, templateCodeId);
    }

}