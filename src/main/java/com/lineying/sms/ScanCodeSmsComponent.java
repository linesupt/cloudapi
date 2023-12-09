package com.lineying.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScanCodeSmsComponent extends BaseSmsComponent {

    @Value("${sms.scancode.sdkAppId}")   // 注入参数值
    private String sdkAppId;

    @Value("${sms.scancode.secretId}")
    private String secretId;

    @Value("${sms.scancode.secretKey}")
    private String secretKey;

    @Value("${sms.scancode.signName}")
    private String signName;

    @Value("${sms.scancode.templateCodeId}")
    private String templateCodeId;

    @Override
    public int sendCode(String phone, String code) {
        return sendCode(sdkAppId, signName, phone, code, templateCodeId);
    }

}
