package com.lineying.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lineying.common.ComponentEnum;
import com.lineying.common.ResultUtil;
import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MaterialMainClass;
import com.lineying.service.IMaterialClassService;
import com.lineying.util.SignUtil;
import com.github.pagehelper.PageInfo;

/**
 * *********************************************
 *
 * @ClassName: MaterialClassController
 * @Description:食材类型控制器
 * @Author ganjing
 * @Date 2019年4月27日
 * @Copyright: 2019 天誉飞歌(重庆)研究院版权所有
 * *********************************************
 */
@RestController
public class MaterialClassController {
    @Autowired
    private IMaterialClassService classService;

    /**
     * ********************************************************
     *
     * @Title: selectMenuPage
     * @Description: 查询食材主分类
     * @Param: @param request
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("/selectMaterialMainClass")
    public String selectMaterialMainClass(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        List<MaterialMainClass> list = classService.selectMaterialMainClass();
        return JSON.toJSONString(ResultUtil.success(list));
    }

    /**
     * ********************************************************
     *
     * @Title: selectMaterialPage
     * @Description: 分页查询食材
     * @Param: @param pageNum
     * @Param: @param pageSize
     * @Param: @param info
     * @Param: @param request
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("searchMaterial")
    public String searchMaterial(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        if (null == map.get("orderByAttr")) {
            map.put("orderByAttr", "bid");
        } else {
            //String sqlName = map.get(ComponentEnum.getValueByMsg(map.get("orderByAttr")));
            map.put("orderByAttr", map.get("orderByAttr").toString());
        }
        if (null == map.get("type")) {
            map.put("type", "asc");
        }
        PageInfo<MaterialClassEntity> page = classService.selectMaterialPage(map);
        return JSON.toJSONString(ResultUtil.success(page));
    }

    /**
     * ********************************************************
     *
     * @Title: getOrderByColumn
     * @Description: 得到排序列
     * @Param: @param request
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("getOrderByColumn")
    public String getOrderByColumn(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        List<String> orderByList = new ArrayList<String>();
        for (ComponentEnum component : ComponentEnum.values()) {
            orderByList.add(component.getName());
        }
        return JSON.toJSONString(ResultUtil.success(orderByList));
    }
}
