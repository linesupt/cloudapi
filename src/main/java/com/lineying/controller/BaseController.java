package com.lineying.controller;

import com.lineying.common.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base controller
 */
public class BaseController {

    protected static final String CHARSET = "utf-8";
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

}
