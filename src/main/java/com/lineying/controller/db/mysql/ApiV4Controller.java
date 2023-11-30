package com.lineying.controller.db.mysql;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.common.CommonConstant;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonCommandEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.JsonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
        }

        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String where = jsonObject.getString("where");
        String sort = jsonObject.getString("sort");
        String sort_column = jsonObject.getString("sort_column");

        Logger.getGlobal().info("执行查询 " + getCurrentTime() + " - " + timestamp + " - " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sort_column);
        List<Map<String, Object>> list = commonService.list(entity);
        return JsonUtil.makeSuccess(JSON.toJSON(list));
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

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
        return JsonUtil.makeResult(result);
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
        }
        String table = jsonObject.getString("table");
        String where = jsonObject.getString("where");

        Logger.getGlobal().info("执行删除 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        boolean result = commonService.delete(entity);
        return JsonUtil.makeResult(result);
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
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
        return JsonUtil.makeResult(result);
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonUtil.makeFailTime();
        }
        String sql = jsonObject.getString("sql");

        Logger.getGlobal().info("执行sql命令 " + key + " - " + data + " - " + signature);
        CommonCommandEntity entity = new CommonCommandEntity();
        entity.setRawSql(sql);
        boolean result = commonService.command(entity);
        return JsonUtil.makeResult(result);
    }

}