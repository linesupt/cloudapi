package com.lineying.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.common.CommonConstant;
import com.lineying.common.LocaleManager;
import com.lineying.data.Param;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * base controller
 */
public class BaseController {

    // 日志
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * 获取当前时间
     * @return
     */
    protected long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取时间戳（ms）
     * @return
     */
    protected long getCurrentTimeMs() {
        return System.currentTimeMillis();
    }

    /**
     * 验证是否执行请求
     * @param timestamp(ms)
     * @return
     */
    protected boolean checkRequest(long timestamp) {
        return Math.abs(getCurrentTime() - timestamp) < CommonConstant.TIME_INTERVAL;
    }

    /**
     * 接口基础验证
     * @param request
     */
    protected Checker doCheck(HttpServletRequest request) {

        String platform = request.getHeader(Param.Key.PLATFORM);
        String locale = request.getHeader(Param.Key.LOCALE);
        String key = request.getParameter(Param.Key.KEY);
        String secretData = request.getParameter(Param.Key.DATA);
        String signature = request.getParameter(Param.Key.SIGNATURE);
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return new Checker(platform, locale, null, JsonCryptUtil.makeFailKey());
            case SIGN_ERROR:
                return new Checker(platform, locale, null, JsonCryptUtil.makeFailSign());
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get(Param.Key.TIMESTAMP).getAsLong();
        if (!checkRequest(timestamp)) {
            return new Checker(platform, locale, null, JsonCryptUtil.makeFailTime());
        }
        return new Checker(platform, locale, jsonObject, null, timestamp);
    }

}
