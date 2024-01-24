package com.lineying.util;

import cn.hutool.crypto.digest.MD5;
import com.lineying.common.CommonConstant;
import com.lineying.common.SecureConfig;
import com.lineying.common.SignResult;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * 签名工具类
 *
 * @author ganjing
 */
public class SignUtil {

    /**
     * 验证签名
     * @param key
     * @param data
     * @param signature
     * @return
     */
    @SignResult
    public static int validateSign(String key, String data, String signature) {
        if (!Objects.equals(key, SecureConfig.DB_API_KEY)) {
            return SignResult.KEY_ERROR; // key error
        }
        String signText = key + data + SecureConfig.DB_SECRET_KEY;
        String signatureText = MD5.create().digestHex(signText);
        Logger.getGlobal().info("执行签名验证 " + signText + " - " + signatureText);
        if (!Objects.equals(signatureText, signature)) {
            return SignResult.SIGN_ERROR; // sign error
        }
        return SignResult.OK;
    }

    /**
     * 得到httpMap
     *
     * @param request
     * @return
     */
    public static Map<String, String> getHttpParamMap(String key, HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        map.put("key", key);
        Map<String, String> resultMap = sortMapByKey(map);
        return resultMap;
    }

    /**
     * 让 Map按key进行排序
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 验证签名
     * @param signp
     * @param map
     * @return
     */
    public static boolean validateSign(String signp, Map<String, String> map) {
        String str = "";
        if (null == map.get(signp)) {
            return false;
        } else {
            String sign = String.valueOf(map.get(signp));
            map.remove(signp);

            if ("-1".equals(sign)) {
                return true;
            }
            for (String s : map.keySet()) {
                if (null == map.get(s)) {
                    continue;
                } else {
                    str += s + "=" + map.get(s) + "&";
                }
            }
            str = str.substring(0, str.length() - 1);
            System.out.println(str);
            if (sign.equals(DigestUtils.md5Hex(str))) {
                return true;
            }
            return false;
        }
    }
}
