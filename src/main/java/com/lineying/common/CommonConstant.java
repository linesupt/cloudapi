package com.lineying.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通用常量
 *
 * @author ganjing
 */
@Component
public class CommonConstant {

    public static String KEY = "";
    public static String SIGN = "sign";
    public static String APPKEY = "";
    public static String IMAGE_PATH = "";
    public static String PIC_PATH = "";
    public static boolean SIGN_TEST_ENABLED = false;
    public static String API_SERVER_BASE_URL = "";
    // 时间误差内允许请求
    public static long TIME_INTERVAL = 60 * 5;

    @Value("${config.image_path}")
    public void setImagePath(String imagesPath) {
        IMAGE_PATH = imagesPath;
    }

    @Value("${config.key}")
    public void setKey(String key) {
        KEY = key;
    }

    @Value("${config.appkey}")
    public void setAppkey(String appkey) {
        APPKEY = appkey;
    }

    @Value("${config.common-pic}")
    public void setPicPath(String pic) {
        PIC_PATH = pic;
    }

    @Value("${config.sign_test_enabled}")
    public void setSignTestEnabled(boolean isEnabled) {
        SIGN_TEST_ENABLED = isEnabled;
    }

    @Value("${config.api_server_base_url}")
    public void setApiServerBaseUrl(String url) {
        API_SERVER_BASE_URL = url;
    }
}
