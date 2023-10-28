package com.lineying.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.lineying.common.CommonConstant;
import com.lineying.common.ResultUtil;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.service.IMenuService;
import com.lineying.util.SignUtil;
import com.github.pagehelper.PageInfo;

@RestController
public class RecommendController {
    @Autowired
    private IMenuService menuService;

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐列表
     * @Param: @param request
     * @Param: @return
     * @Return: ModelAndView
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("selectRecommendList")
    public ModelAndView selectRecommendList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("recommendList");
        PageInfo<MenuInfoEntity> page = menuService.selectRecommendListPage(null);
        mv.addObject("list", page);
        return mv;
    }

    /**
     * ********************************************************
     *
     * @Title: updateRecommend
     * @Description: 更新推荐
     * @Param: @param request
     * @Param: @param password
     * @Param: @param id
     * @Param: @param status
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("updateRecommend")
    @ResponseBody
    public String updateRecommend(HttpServletRequest request, String password,
                                  String id, String status) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", status);
        if (!CommonConstant.KEY.equals(password)) {
            return JSON.toJSONString(ResultUtil.error("输入的密码不正确"));
        } else {
            boolean b = menuService.updateRecommend(param);
            if (b) {
                return JSON.toJSONString(ResultUtil.success(b));
            } else {
                return JSON.toJSONString(ResultUtil.error("修改推荐列表失败"));
            }
        }
    }

    /**
     * ********************************************************
     *
     * @Title: addRecommend
     * @Description:新增推荐
     * @Param: @param pageNum
     * @Param: @param pageSize
     * @Param: @param info
     * @Param: @param request
     * @Param: @return
     * @Return: ModelAndView
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("addRecommend")
    public ModelAndView addRecommend(int pageNum, int pageSize, String id,
                                     String name, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("addRecommend");
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        PageInfo<MenuInfoEntity> page = menuService.selectNoRecommendList(map);
        mv.addObject("list", page);
        mv.addObject("name", name);
        mv.addObject("pageNum", pageNum);
        mv.addObject("pageSize", pageSize);
        return mv;
    }
}
