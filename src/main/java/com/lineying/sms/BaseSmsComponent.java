package com.lineying.sms;

import com.lineying.common.CommonConstant;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.Region;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import java.util.logging.Logger;

/**
 * 消息发送
 */
public abstract class BaseSmsComponent {

    /**
     * 获取SmsClient客户端
     * @return
     */
    public SmsClient getClient() {
        Credential cred = new Credential(CommonConstant.TENCENTCLOUD_SECRET_ID, CommonConstant.TENCENTCLOUD_SECRET_KEY);
        return new SmsClient(cred, Region.Shanghai.getValue());
    }

    /**
     * 发送验证码
     * @param phone
     * @param code
     * @return
     */
    protected abstract int sendCode(String phone, String code);

    /**
     * 发送验证码
     * @param sdkAppId
     * @param signName
     * @param phone
     * @param code
     * @param templateCodeId
     * @return
     */
    public int sendCode(String sdkAppId, String signName, String phone, String code, String templateCodeId) {
        SendSmsResponse resp;
        try {
            SendSmsRequest req = makeRequest(sdkAppId, signName, phone, code, templateCodeId);
            resp = getClient().SendSms(req);
            Logger.getGlobal().info(SendSmsResponse.toJsonString(resp));   // 把返回信息输入到日志中
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 获取req请求 2个参数，就是短息模板是两个参数，根据具体情况改变
     *
     * @param phone
     * @param code  参数1，这里是验证码
     *              param2为参数2这里为分钟
     * @param templateId  短息模板id
     * @return
     */
    public static SendSmsRequest makeRequest(String sdkAppId, String signName, String phone, String code, String templateId) {
        SendSmsRequest req = new SendSmsRequest();
        String targetPhone = phone;

        if (!phone.startsWith("+86")) {
            targetPhone = "+86" + phone;
        }
        String[] phoneNumberSet = { targetPhone };
        req.setSmsSdkAppId(sdkAppId);
        req.setPhoneNumberSet(phoneNumberSet);
        req.setSignName(signName);
        req.setTemplateId(templateId);
        String[] templateParamSet = { code };
        req.setTemplateParamSet(templateParamSet);
        return req;
    }

}
