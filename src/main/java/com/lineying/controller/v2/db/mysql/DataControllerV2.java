package com.lineying.controller.v2.db.mysql;

import com.lineying.controller.api.db.mysql.DataController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据级接口控制
 * 增加了token认证
 */
@RestController
@RequestMapping("v2/db/mysql")
public class DataControllerV2 extends DataController {

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {
        return super.select(request);
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {
        return super.insert(request);
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {
        return super.delete(request);
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {
        return super.update(request);
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {
        return super.command(request);
    }

}