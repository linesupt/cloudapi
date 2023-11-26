package com.lineying.controller.db.mysql;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * 接口控制
 */
@RestController
@RequestMapping("api/db/mysql")
public class ApiController {

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        Logger.getGlobal().info("执行添加");
        // 执行删除
        return "";
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        Logger.getGlobal().info("执行删除");
        return "";
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        Logger.getGlobal().info("执行更新");
        return "";
    }

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        Logger.getGlobal().info("执行查询");
        return "";
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        Logger.getGlobal().info("执行sql命令");
        return "";
    }

}