package com.lineying.controller.db.mysql;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.common.CommonConstant;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonCommandEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.JsonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.lineying.controller.db.mysql.ApiV4Controller.SignResult.*;

/**
 * 接口控制
 */
@RestController
@RequestMapping("v4/db/mysql")
public class ApiV4Controller {

    @Resource
    ICommonService commonService;

    private long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 验证是否执行请求
     * @param timestamp
     * @return
     */
    private boolean checkRequest(long timestamp) {
        return Math.abs(getCurrentTime() - timestamp) < CommonConstant.TIME_INTERVAL;
    }

    /**
     * 签名验证结果
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface SignResult {
        int OK = 1;
        int KEY_ERROR = 2; // 公钥不匹配
        int SIGN_ERROR = 3; // 签名错误
    }

    /**
     * 验证签名
     * @param key
     * @param data
     * @param signature
     * @return
     */
    private @SignResult int validateSign(String key, String data, String signature) {
        if (!Objects.equals(key, CommonConstant.DB_API_KEY)) {
            return SignResult.KEY_ERROR; // key error
        }
        String signText = key + data + CommonConstant.DB_SECRET_KEY;
        String signatureText = MD5.create().digestHex(signText);
        Logger.getGlobal().info("执行签名验证 " + signText + " - " + signatureText);
        if (!Objects.equals(signatureText, signature)) {
            return SignResult.SIGN_ERROR; // sign error
        }
        return OK;
    }

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String where = jsonObject.getString("where");
        String sort = jsonObject.getString("sort");
        String sort_column = jsonObject.getString("sort_column");

        Logger.getGlobal().info("执行查询 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sort_column);
        List<Map<String, Object>> list = commonService.list(entity);
        JSONObject obj = new JSONObject();
        obj.put("data", JSON.toJSON(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
        }
        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String value = jsonObject.getString("value");

        Logger.getGlobal().info("执行添加 " + key + " - " + data + " - " + signature);
        CommonAddEntity addEntity = new CommonAddEntity();
        addEntity.setTable(table);
        addEntity.setColumn(column);
        addEntity.setValue(value);
        boolean result = commonService.add(addEntity);
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String table = jsonObject.getString("table");
        String where = jsonObject.getString("where");

        Logger.getGlobal().info("执行删除 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        boolean result = commonService.delete(entity);
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String table = jsonObject.getString("table");
        String set = jsonObject.getString("set");
        String where = jsonObject.getString("where");

        Logger.getGlobal().info("执行更新 " + key + " - " + data + " - " + signature);

        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        boolean result = commonService.update(entity);
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");

        int signResult = validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String sql = jsonObject.getString("sql");

        Logger.getGlobal().info("执行sql命令 " + key + " - " + data + " - " + signature);
        CommonCommandEntity entity = new CommonCommandEntity();
        entity.setRawSql(sql);
        boolean result = commonService.command(entity);
        return JsonCryptUtil.makeResult(result);
    }

}