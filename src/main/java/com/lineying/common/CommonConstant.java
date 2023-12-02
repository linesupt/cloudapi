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
    // apikey，用于查找所属项目，暂未使用
    public static String DB_API_KEY = "";
    // 加密密钥
    public static String DB_SECRET_KEY = "";
    // 首次分块加密偏移量
    public static String IV_SEED = "";
    // 时间误差内允许请求
    public static long TIME_INTERVAL = 24 * 60 * 500;

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

    @Value("${config.db_api_key}")
    public void setDbApiKey(String dbApiKey) {
        DB_API_KEY = dbApiKey;
    }

    @Value("${config.db_secret_key}")
    public void setDbSecretKey(String dbSecretKey) {
        DB_SECRET_KEY = dbSecretKey;
    }

    @Value("${config.iv_seed}")
    public void setIvSeed(String ivSeed) {
        IV_SEED = ivSeed;
    }
}
