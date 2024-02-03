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
        // 不需要登录的功能:登录、短信、支付通知、云数据查询/添加
        excludePath.add("/api/**");
        excludePath.add("**/login");
        excludePath.add("**/cloud/add");
        excludePath.add("**/cloud/select");
        excludePath.add("**/verify/send_code");
        excludePath.add("**/verify/code_verify");
        excludePath.add("**/pay/alipay/notify");
        excludePath.add("**/pay/wxpay/notify");
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);//除了登陆接口其他所有接口都需要token验证
    }

}
