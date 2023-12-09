package com.lineying.controller.verify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.bean.VerifyCode;
import com.lineying.common.SignResult;
import com.lineying.controller.BaseController;
import com.lineying.mail.EmailSenderManager;
import com.lineying.sms.SmsSenderManager;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import com.lineying.util.VerifyCodeGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * 应用级接口,验证码验证
 */
@RestController
@RequestMapping("api/verify")
public class VerifyController extends BaseController {

    // 验证码有效时间
    public static final int VERIFY_INTERVAL = 2 * 60;
    public static final int VERIFY_INTERVAL_CLEAR = 24 * 3600;

    private static Map<Integer, VerifyCode> mVerifyCodes = new HashMap<>();

    private List<String> mAppServers = Arrays.asList("mathcalc", "scancode", "linevideo");

    //@Resource
    //IVerifyCodeService verifyCodeService;

    /**
     * 执行验证码缓存清除
     */
    private void clearVerifyCodes() {
        Set<Integer> keySet = mVerifyCodes.keySet();
        Iterator<Integer> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            int uid = iterator.next();
            VerifyCode entity = mVerifyCodes.get(uid);
            if (getCurrentTime() - entity.getTimestamp() > VERIFY_INTERVAL_CLEAR) {
                mVerifyCodes.remove(uid);
            }
        }
    }

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("/send_code")
    public String sendCode(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        String locale = request.getHeader("locale");
        Logger.getGlobal().info("platform:" + platform + " locale:" + locale);
        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        Logger.getGlobal().info("接收消息::" + key + " - " + secretData + " - " + signature);
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case SignResult.KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SignResult.SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String sendCode = VerifyCodeGenerator.generate();
        // 生成验证码, 执行邮件发送逻辑
        String app = jsonObject.getString("app");
        int uid = jsonObject.getInteger("uid");
        int type = jsonObject.getInteger("type");
        String target = jsonObject.getString("target");
        String subject = "Verify Code";
        String content = String.format("您的验证码是：%s，请尽快进行验证", sendCode);
        int sendResult = 0;
        if (!mAppServers.contains(app)) {
            Logger.getGlobal().info("不存在当前应用::" + app);
            return JsonCryptUtil.makeFailSendVerifyCode();
        }
        if (type == 1) {
            // 处理邮件发送
            sendResult = EmailSenderManager.relayEmail(subject, content, target);
            if (sendResult == 0) {
                Logger.getGlobal().info("邮件发送失败!");
            }
        } else if (type == 2) {
            // 处理短信发送
            sendResult = SmsSenderManager.relaySms(subject, content, target);
            if (sendResult == 0) {
                Logger.getGlobal().info("短信发送失败!");
            }
        }

        if (sendResult == 0) {
            return JsonCryptUtil.makeFailSendVerifyCode();
        }

        clearVerifyCodes(); // 执行清理

        Logger.getGlobal().info("生成验证码::" + sendCode);
        VerifyCode entity = new VerifyCode();
        entity.setUid(uid);
        entity.setApp(app);
        entity.setCode(sendCode);
        entity.setTarget(target);
        entity.setType(type);
        entity.setTimestamp(getCurrentTime());
        mVerifyCodes.put(uid, entity);
        return JsonCryptUtil.makeSuccess();
    }

    /**
     * 短信验证
     * @return
     */
    @RequestMapping("/code_verify")
    public String codeVerify(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        String locale = request.getHeader("locale");
        Logger.getGlobal().info("platform:" + platform + " locale:" + locale);
        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case SignResult.KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SignResult.SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String app = jsonObject.getString("app");
        int uid = jsonObject.getInteger("uid");
        String code = jsonObject.getString("code");
        int type = jsonObject.getInteger("type");

        long limitTimestamp = getCurrentTime() - VERIFY_INTERVAL;
        VerifyCode entity = mVerifyCodes.get(uid);
        if (entity == null) {
            return JsonCryptUtil.makeFailVerifyCode();
        } else {
            if (!Objects.equals(app, entity.getApp())) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            if (type != entity.getType()) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            // 对比验证码
            String serverCode = entity.getCode().toLowerCase();
            String targetCode = code.toLowerCase();
            if (!Objects.equals(serverCode, targetCode)) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            // 对比时间
            if (entity.getTimestamp() < limitTimestamp) {
                // 验证码已经过期
                return JsonCryptUtil.makeFailVerifyTimeout();
            }
        }

        return JsonCryptUtil.makeSuccess();
    }

}