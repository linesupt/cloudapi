package com.lineying.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.entity.CommonQueryEntity;
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
@RequestMapping("mysql")
public class ApiTestController {
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

        Logger.getGlobal().info("执行查询 " + key + " - " + data + " - " + signature + " - "
                + timestamp + " - " + table + " - " + column + " - " + where + " - " + sort + " - " + sort_column);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sort_column);
        List<Map<String, Object>> list = commonService.list(entity);
        return JSONUtil.toJsonStr(list);
    }


}