package com.lineying.controller.db.mysql;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import org.springframework.web.bind.annotation.RequestBody;
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

        Logger.getGlobal().info("执行查询 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sort_column);
        List<Map<String, Object>> list = commonService.list(entity);
        return JSONUtil.toJsonStr(list);
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String column = jsonObject.getString("column");
        String value = jsonObject.getString("value");

        Logger.getGlobal().info("执行添加 " + key + " - " + data + " - " + signature);
        CommonAddEntity addEntity = new CommonAddEntity();
        addEntity.setTable(table);
        addEntity.setColumn(column);
        addEntity.setValue(value);
        commonService.add(addEntity);
        return "新增成功";
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String where = jsonObject.getString("where");

        Logger.getGlobal().info("执行删除 " + key + " - " + data + " - " + signature);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        commonService.delete(entity);
        return "删除成功";
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String table = jsonObject.getString("table");
        String set = jsonObject.getString("set");
        String where = jsonObject.getString("where");

        Logger.getGlobal().info("执行更新 " + key + " - " + data + " - " + signature);

        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        commonService.update(entity);
        return "更新成功";
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        String key = request.getParameter("key");
        String data = request.getParameter("data");
        String signature = request.getParameter("signature");

        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        String sql = jsonObject.getString("sql");

        Logger.getGlobal().info("执行sql命令 " + key + " - " + data + " - " + signature);
        return key;
    }

}