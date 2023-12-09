package com.lineying.sms;

import com.lineying.common.CommonConstant;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Region;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import java.util.logging.Logger;

public class BaseSmsComponent {

    /**
     * 获取SmsClient客户端
     * @return
     */
    public SmsClient getClient() {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(CommonConstant.TENCENTCLOUD_SECRET_ID, CommonConstant.TENCENTCLOUD_SECRET_KEY);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        //这个setEndpoint可以省略的
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        return new SmsClient(cred, Region.Shanghai.getValue(), clientProfile);
    }

    protected int sendCode(String phone, String code) {
        // TODO
        return 0;
    }

    public int sendCode(String sdkAppId, String signName, String phone, String code, String templateCodeId) {

        // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
        SendSmsResponse resp;
        try {
            resp = getClient().SendSms(getReqTwo(sdkAppId, signName, phone, code, templateCodeId));  // 模板id是自己设置好的
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
    public static SendSmsRequest getReqTwo(String sdkAppId, String signName, String phone, String code, String templateId) {
        SendSmsRequest req = new SendSmsRequest();
        String targetPhone = phone;

        if (!phone.startsWith("+86")) {
            targetPhone = "+86" + phone;
        }
        String[] phoneNumberSet = { targetPhone };
        req.setSmsSdkAppId(sdkAppId); // 设置参数
        req.setPhoneNumberSet(phoneNumberSet);
        req.setSignName(signName);
        req.setTemplateId(templateId);
        //模板内容的参数有几个就设置几个，我这里是两个
        String[] templateParamSet = { code };
        req.setTemplateParamSet(templateParamSet);
        return req;     // 返回请求参数内容
    }

}
