package com.lineying.manager;

import com.lineying.entity.ApiLog;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.IPUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        ApiLog log = new ApiLog();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        String headerString = headerMap.toString();
        if (headerString.length() > 255) {
            headerString = headerString.substring(0, 255);
        }
        Map<String, String> paramMap = new HashMap<>();
        // 获取所有请求参数的名称
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            paramMap.put(key, Arrays.toString(values));
        }
        String paramString = paramMap.toString();
        String ipaddr = IPUtil.getIpAddress(request);
        log.setApiName("");
        log.setApiUri(request.getRequestURI());
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
    }

}
