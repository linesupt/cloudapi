package com.lineying.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试使用
 */
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * @return
     */
    @RequestMapping("test")
    public String test() {
        return "hello";
    }

    /**
     * a，b为参数，time时间或者随机数。sign为签名值
     *
     * @param a
     * @param b
     * @param time
     * @param sign
     * @return
     */
    //@RequestMapping("sign")
    public String sign(String a, String b, String time, String sign) {
        //String key = "123456";
        //先排序a-z得到的参数。参数名a的值为c.以a=c&b=d的格式(a参数名，c为参数值),time为随机参数或者时间
        //  String str = "a=" + a + "&b=" + b + "&key=" + key + "&time=" + time;
        //加密得到
        //System.out.println(MD5Encoder.encode("a=c&b=d&key=11&time=1".getBytes()));
        return DigestUtils.md5Hex("a=c&b=d&key=11&time=1");
    }

}
