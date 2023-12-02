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

    // 公钥，用于查找所属项目
    public static String DB_API_KEY = "";
    // 加密密钥
    public static String DB_SECRET_KEY = "";
    // 首次分块加密偏移量
    public static String IV_SEED = "";
    // 时间误差内允许请求
    public static long TIME_INTERVAL = 60 * 2;

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
