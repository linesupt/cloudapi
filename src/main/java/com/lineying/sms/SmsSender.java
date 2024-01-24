package com.lineying.sms;

import com.lineying.common.CommonConstant;
import com.lineying.common.SecureConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Region;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

import java.util.logging.Logger;

/**
 * 消息发送
 */
public class SmsSender {

    /**
     * 获取SmsClient客户端
     * @return
     */
    public SmsClient getClient() {
        Credential cred = new Credential(SecureConfig.TENCENTCLOUD_SECRET_ID, SecureConfig.TENCENTCLOUD_SECRET_KEY);
        // 实例化一个http选项，可选，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        //创建了一个ClientProfile对象clientProfile，并设置签名方法为"HmacSHA256"，并将上一步创建的httpProfile设置到其中
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, Region.Beijing.getValue(), clientProfile);
    }

    /**
     * 发送验证码
     * @param phone
     * @param code
     * @param appId
     * @param signName
     * @param templateId
     * @return
     */
    public int sendCode(String phone, String code, String appId, String signName, String templateId) {
        SendSmsResponse resp;
        try {
            SendSmsRequest req = makeRequest(phone, code, appId, signName, templateId);
            resp = getClient().SendSms(req);
            SendStatus[] status = resp.getSendStatusSet();
            Logger.getGlobal().info("status::" + status);
            Logger.getGlobal().info("发送验证码结果::" + SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 生成短信请求
     * @param phone 手机号码
     * @param code 验证码
     * @param appId 短息应用ID
     * @param signName 签名内容
     * @param templateId 短息模板id
     * @return
     */
    public static SendSmsRequest makeRequest(String phone, String code, String appId, String signName, String templateId) {
        SendSmsRequest req = new SendSmsRequest();
        String targetPhone = phone;
        if (!phone.startsWith("+86")) {
            targetPhone = "+86" + phone;
        }
        String[] phoneNumberSet = {targetPhone};
        req.setSmsSdkAppId(appId);
        req.setPhoneNumberSet(phoneNumberSet);
        req.setSignName(signName);
        req.setTemplateId(templateId);
        String[] templateParamSet = {code};
        req.setTemplateParamSet(templateParamSet);
        return req;
    }

}
