package com.lineying.manager;

import com.lineying.entity.ApiLog;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.IPUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * API日志管理
 */
public class ApiLogManager {

    private ApiLogManager() { }

    /**
     * 持久化接口日志
     * @param request
     * @param appcode 应用代码
     * @param uid 用户ID
     * @param body 请求体
     * @param data 响应内容
     */
    public static void saveLog(ICommonService commonService, HttpServletRequest request, String apiName, String appcode, int uid, String body, String data) {
        ApiLog log = new ApiLog();
        String ipaddr = IPUtil.getIpAddress(request);
        log.setAppcode(appcode);
        log.setUid(uid);
        log.setName(apiName);
        log.setUri(request.getRequestURI());
        log.setIpaddr(ipaddr);
        log.setContentType(request.getContentType());
        long timestamp = System.currentTimeMillis();
        log.setCreateTime(timestamp);
        log.setUpdateTime(timestamp);
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headerMap = new HashMap<>();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                //Logger.getGlobal().info("key:" + key + " value:" + value);
                headerMap.put(key, value);
            }
            String headerString = headerMap.toString();
            if (headerString.length() > 500) {
                headerString = headerString.substring(0, 500);
            }
            String bodyParam = body;
            if ("".equals(body)) {
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
                bodyParam = paramString;
            }
            log.setHeader(headerString);
            log.setBody(bodyParam);
            log.setData(data);
            log.setModel("");
            boolean flag = commonService.add(CommonSqlManager.addColumnData(TableManager.getApiLogTable(), log.getColumn(), log.getValue()));
            System.out.println("logger::" + uid + " - " + flag + " - " + log);
        } catch (Exception e) {
            log.setName("日志异常");
            log.setData(e.getMessage());
            boolean flag = commonService.add(CommonSqlManager.addColumnData(TableManager.getApiLogTable(), log.getColumn(), log.getValue()));
            System.out.println("logger::" + flag + " - " + log);
            e.printStackTrace();
        }
    }

}
