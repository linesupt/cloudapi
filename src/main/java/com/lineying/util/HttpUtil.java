package com.lineying.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Http工具
 */
public class HttpUtil {

    /**
     * 读取原始数据
     * @param request
     * @return
     */
    public static String readRaw(HttpServletRequest request) {
        try {
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String rawData = sb.toString();
            return rawData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
