package com.lineying.controller;

import com.lineying.common.CommonConstant;
import com.lineying.data.Param;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * token认证拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String servletPath = request.getServletPath();
        System.out.println("api path::" + servletPath);
        response.setCharacterEncoding(CommonConstant.CHARSET);
        String token = request.getHeader(Param.Key.TOKEN);
        int resultCode = -1;
        //System.out.println("token::" + token);
        if (!"".equals(token) && !"null".equals(token)) {
            try {
                resultCode = TokenUtil.verify(token);
            } catch (Exception e) { e.printStackTrace(); }
            if(resultCode == 0){
                System.out.println("通过拦截器");
                return true;
            }
        }
        response.setCharacterEncoding(CommonConstant.CHARSET);
        response.setContentType(CommonConstant.CONTENT_TYPE);
        String result = JsonCryptUtil.makeFail("token verify fail " + resultCode);
        response.getWriter().append(result);
        return false;
    }

}

