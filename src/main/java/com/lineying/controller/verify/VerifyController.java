package com.lineying.controller.verify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.common.SignResult;
import com.lineying.controller.BaseController;
import com.lineying.entity.VerifyCodeEntity;
import com.lineying.mail.EmailSenderManager;
import com.lineying.service.IVerifyCodeService;
import com.lineying.sms.SmsSenderManager;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import com.lineying.util.VerifyCodeGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * 应用级接口,验证码验证
 */
@RestController
@RequestMapping("api/verify")
public class VerifyController extends BaseController {

    // 验证码有效时间
    public static final int VERIFY_INTERVAL = 2 * 60_000;

    @Resource
    IVerifyCodeService verifyCodeService;

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("/send_code")
    public String sendCode(HttpServletRequest request) {
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
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String verifyCode = VerifyCodeGenerator.generate();
        // 生成验证码, 执行邮件发送逻辑
        String table = jsonObject.getString("table");
        int uid = jsonObject.getInteger("uid");
        String code = jsonObject.getString("code");
        String target = jsonObject.getString("target");
        int type = jsonObject.getInteger("type");
        String subject = "Verify Code";
        String content = String.format("您的验证码是：%s，请尽快进行验证", verifyCode);
        int sendResult = 0;
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

        VerifyCodeEntity entity = new VerifyCodeEntity();
        entity.setTable(table);
        entity.setColumn("`uid`,`code`,`target`,`type`,`create_time`,`update_time`");
        String value = String.format("'%s','%s','%s','%s','%s','%s'", uid + "", code, target, type + "", timestamp  + "", timestamp + "");
        entity.setValue(value);
        boolean result = verifyCodeService.add(entity);
        return JsonCryptUtil.makeResult(result);
    }

    /**
     * 短信验证
     * @return
     */
    @RequestMapping("/code_verify")
    public String codeVerify(HttpServletRequest request) {
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
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String table = jsonObject.getString("table");
        int uid = jsonObject.getInteger("uid");
        String code = jsonObject.getString("code");
        String target = jsonObject.getString("target");
        int type = jsonObject.getInteger("type");

        long limitTimestamp = timestamp - VERIFY_INTERVAL;
        VerifyCodeEntity entity = new VerifyCodeEntity();
        entity.setTable(table);
        entity.setColumn("`uid`,`code`,`target`,`type`,`create_time`,`update_time`");
        String value = String.format("'%s','%s','%s','%s','%s','%s'", uid + "", code, target, type + "", timestamp  + "", timestamp + "");
        entity.setValue(value);
        entity.setWhere("lower(code) = " + code + " and create_time > " + limitTimestamp);
        entity.setSortColumn("create_time");
        entity.setSort("desc");
        List<Map<String, Object>> list = verifyCodeService.list(entity);
        Logger.getGlobal().info("查询结果::" + list);
        if (list.size() == 0) {
            return JsonCryptUtil.makeFailVerifyCode();
        }

        return JsonCryptUtil.makeSuccess();
    }

}