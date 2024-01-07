package com.lineying.controller.db.mysql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.controller.BaseController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonCommandEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.JsonUtil;
import com.lineying.util.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.lineying.common.SignResult.*;

/**
 * 数据级接口控制
 */
@RestController
@RequestMapping("api/db/mysql")
public class DataController extends BaseController {

    @Resource
    ICommonService commonService;

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String table = jsonObject.get("table").getAsString();
        String column = jsonObject.get("column").getAsString();
        String where = jsonObject.get("where").getAsString();
        String sort = jsonObject.get("sort").getAsString();
        String sort_column = jsonObject.get("sort_column").getAsString();

        Logger.getGlobal().info("执行查询 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sort_column);

        List<Map<String, Object>> list;
        try {
            list = commonService.list(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
        }
        String table = jsonObject.get("table").getAsString();
        String column = jsonObject.get("column").getAsString();
        String value = jsonObject.get("value").getAsString();

        Logger.getGlobal().info("执行添加 " + key + " - " + data + " - " + signature);
        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setValue(value);
        boolean result = false;
        try {
            result = commonService.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String table = jsonObject.get("table").getAsString();
        String where = jsonObject.get("where").getAsString();

        Logger.getGlobal().info("执行删除 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        boolean result = false;
        try {
            result = commonService.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String table = jsonObject.get("table").getAsString();
        String set = jsonObject.get("set").getAsString();
        String where = jsonObject.get("where").getAsString();

        Logger.getGlobal().info("执行更新 " + key + " - " + data + " - " + signature);

        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        boolean result = false;
        try {
            result = commonService.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");

        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }
        String sql = jsonObject.get("sql").getAsString();

        Logger.getGlobal().info("执行sql命令 " + key + " - " + data + " - " + signature);
        CommonCommandEntity entity = new CommonCommandEntity();
        entity.setRawSql(sql);
        boolean result = false;
        try {
            result = commonService.command(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

}