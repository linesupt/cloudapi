package com.lineying.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器配置
 */
@Component
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private TokenInterceptor tokenInterceptor;

    //构造方法
    public InterceptorConfig(TokenInterceptor tokenInterceptor){
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        List<String> excludePath = new ArrayList<>();
        // 不需要登录的功能:登录、短信、支付通知、云数据查询/添加、修改密码/反馈建议
        excludePath.add("/api/**");
        excludePath.add("/v2/verify/**");
        excludePath.add("**/login");
        excludePath.add("**/register");
        excludePath.add("**/feedback/add");
        excludePath.add("**/cloud/add");
        excludePath.add("**/cloud/select");
        excludePath.add("**/verify/send_code");
        excludePath.add("**/verify/code_verify");
        excludePath.add("/api/pay/alipay/notify");
        excludePath.add("/api/pay/wxpay/notify");
        excludePath.add("/v3/pay/alipay/notify");
        excludePath.add("/v3/pay/wxpay/notify");
        //excludePath.add("/api/pay/apple/notify");
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
