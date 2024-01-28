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

    // 时间误差内允许请求
    public static long TIME_INTERVAL = 60 * 2;

    // 公钥，用于查找所属项目
    public static String BASE_URL;
    @Value("${config.base_url}")
    public void setDbApiKey(String baseUrl) {
        BASE_URL = baseUrl;
    }

}
