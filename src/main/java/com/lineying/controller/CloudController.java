package com.lineying.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("api")
public class CloudController {

    /**
     * 服务器时间戳（s）
     * @return
     */
    @RequestMapping("/timestamp")
    public long timestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 服务器时间戳（ms）
     * @return
     */
    @RequestMapping("/mstimestamp")
    public long timestampMillis() {
        return System.currentTimeMillis();
    }

}
