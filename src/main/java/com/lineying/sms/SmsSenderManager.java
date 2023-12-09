package com.lineying.sms;

import com.lineying.common.CommonConstant;
import com.lineying.sms.tencentcloud.SmsClient;
import com.lineying.sms.tencentcloud.models.SendSmsRequest;
import com.lineying.sms.tencentcloud.models.SendSmsResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.util.logging.Logger;

/**
 * 短信发送
 */
public class SmsSenderManager {


    /**
     * 发送内容至目标手机
     */
    public static int relaySms(String subject, String content, String mobile) {
        // TODO
        return 1;
    }


}
