package com.lineying.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试使用
 */
@RestController
@RequestMapping("api")
public class CloudController {

    /**
     * 服务器时间戳
     * @return
     */
    @RequestMapping("/timestamp")
    public long timestamp() {
        return System.currentTimeMillis() / 1000;
    }

    @RequestMapping("/timestampms")
    public long timestampMillis() {
        return System.currentTimeMillis();
    }

}
