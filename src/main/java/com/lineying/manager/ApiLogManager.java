package com.lineying.manager;

import com.lineying.controller.Checker;
import com.lineying.data.Param;
import com.lineying.entity.ApiLog;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.IPUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * API日志管理
 */
public class ApiLogManager {

    private ApiLogManager() { }

    /**
     * 持久化接口日志
     * @param request
     */
    public static void saveLog(ICommonService commonService, HttpServletRequest request) {
        try {
            ApiLog log = new ApiLog();
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headerMap = new HashMap<>();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                headerMap.put(key, value);
            }
            String headerString = headerMap.toString();
            if (headerString.length() > 500) {
                headerString = headerString.substring(0, 500);
            }
            Map<String, String> paramMap = new HashMap<>();
            // 获取所有请求参数的名称
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String[] values = request.getParameterValues(key);
                String value = "";
                if (values.length > 0) {
                    value = values[0];
                }
                paramMap.put(key, value);
            }
            String paramString = paramMap.toString();
            if (paramString.length() > 500) {
                paramString = paramString.substring(0, 500);
            }
            String ipaddr = IPUtil.getIpAddress(request);
            log.setName("");
            log.setUri(request.getRequestURI());
            log.setIpaddr(ipaddr);
            log.setContentType(request.getContentType());
            log.setHeader(headerString);
            log.setBody(paramString);
            log.setData("");
            log.setModel("");
            long timestamp = System.currentTimeMillis();
            log.setCreateTime(timestamp);
            log.setUpdateTime(timestamp);
            boolean flag = commonService.add(CommonSqlManager.addColumnData(TableManager.getApiLogTable(), log.getColumn(), log.getValue()));
            System.out.println("logger::" + flag + " - " + log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
