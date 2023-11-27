package com.lineying.controller.db.mysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * 接口控制
 */
@RestController
@RequestMapping("v4/db/mysql")
public class ApiV4Controller {

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String where = jsonObject.getString("where");
        String sort = jsonObject.getString("sort");
        String sort_column = jsonObject.getString("sort_column");

        Logger.getGlobal().info("执行添加 " + key + " - " + data + " - " + signature + " - "
                + timestamp + " - " + table + " - " + column + " - " + where + " - " + sort + " - " + sort_column);
        return key;
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        Logger.getGlobal().info("执行删除 " + key + " - " + data + " - " + signature);
        return key;
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String where = jsonObject.getString("where");
        String sort = jsonObject.getString("sort");
        String sort_column = jsonObject.getString("sort_column");

        Logger.getGlobal().info("执行更新 " + key + " - " + data + " - " + signature + " - "
                + timestamp + " - " + table + " - " + column + " - " + where + " - " + sort + " - " + sort_column);
        return key;
    }

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String where = jsonObject.getString("where");
        String sort = jsonObject.getString("sort");
        String sort_column = jsonObject.getString("sort_column");

        Logger.getGlobal().info("执行查询 " + key + " - " + data + " - " + signature + " - "
                + timestamp + " - " + table + " - " + column + " - " + where + " - " + sort + " - " + sort_column);
        return key;
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        Logger.getGlobal().info("执行sql命令 " + key + " - " + data + " - " + signature);
        return key;
    }

}