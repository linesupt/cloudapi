package com.lineying.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lineying.common.CommonConstant;
import com.lineying.common.ResultUtil;
import com.lineying.entity.MenuClassEntity;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.service.IMenuClassService;
import com.lineying.service.IMenuManagerService;
import com.lineying.service.IMenuService;
import com.lineying.util.SignUtil;
import com.github.pagehelper.PageInfo;

/**
 * 开放接口管理
 *
 * @author ganjing
 * @date 2019年3月6日 上午11:57:47
 */
@RestController
public class MenuManagerController {

    @Autowired
    private IMenuManagerService menuManagerService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuClassService menuClassService;

    /**
     * @param pageNum
     * @param pageSize
     * @param info
     * @param request  测试时注意资源的端口，需要手动启动tomcat才能访问
     *                 http://localhost:8080/recipe/searchRecipe?pageNum=1&pageSize=10&classId=5&sign=-1
     * @return
     */
    @RequestMapping("searchRecipe")
    public String searchRecipe(int pageNum, int pageSize, MenuInfoEntity info,
                               HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        PageInfo<MenuInfoEntity> page = menuManagerService.selectMenuPage(pageNum, pageSize, info);
        return JSON.toJSONString(ResultUtil.success(page));
    }

    /**
     * 查询菜单类型
     * 可选参数(默认值1,20)：pageNum = 1, pageSize = 20
     * 可选限定参数: parentid | name
     *
     * @param request
     * @return
     */
    @RequestMapping("selectMenuClassPage")
    public String selectMenuClassPage(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        PageInfo<MenuClassEntity> result = menuClassService.selectMenuClassPage(map);
        return JSON.toJSONString(ResultUtil.success(result));
    }

    /**
     * ********************************************************
     *
     * @Title: selectMenuClassPage
     * @Description: 查询所有分类
     * @Param: @param request
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("selectAllMenuClass")
    public String selectAllMenuClass(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        List<MenuClassEntity> result = menuClassService.selectAllMenuClass();
        return result.toString();
    }

    /**
     * 查询菜单详情
     * 传入mid对应菜单id,
     *
     * @param request
     * @return
     */
    @RequestMapping("selectMenuDetailById")
    public String selectMenuDetailById(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        String id = String.valueOf(map.get("mid"));
        MenuInfoEntity menu = menuService.selectMenuDetailById(id);
        menu.setContent(menu.getContent().replaceAll("<br />", ""));
        return JSON.toJSONString(ResultUtil.success(menu));
    }

    /**
     * 验证签名结果
     *
     * @param request
     * @return
     */
    @RequestMapping("sign")
    public String sign(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        String sign = String.valueOf(map.get(CommonConstant.SIGN));
        map.remove(CommonConstant.SIGN);

        StringBuilder sb = new StringBuilder();
        for (String s : map.keySet()) {
            if (null == map.get(s)) {
                continue;
            } else {
                sb.append(s).append("=").append(map.get(s)).append("&");
            }
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        String calSign = DigestUtils.md5Hex(result);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("calSign", calSign);
        resultMap.put("sign", sign);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 根据id更新数据
     *
     * @return
     */
    //@RequestMapping("updateMenuInfoBySId")
    public String updateMenuInfoBySId(MenuInfoEntity info) {
        boolean b = menuManagerService.updateMenuInfoBySId(info);
        return JSON.toJSONString(b);
    }

    /**
     * 根据id更新数据
     *
     * @return
     */
    //@RequestMapping("deleteMenuBySId")
    public String deleteMenuBySId() {
        List<String> list1 = new ArrayList<String>();
        list1.add("666");
        boolean b = menuManagerService.deleteMenuBySId(list1);
        return JSON.toJSONString(b);
    }

    /**
     * 根据id更新数据
     *
     * @return
     */
    //@RequestMapping("deleteMenuClassBySId")
    public String deleteMenuClassBySId() {
        List<String> list1 = new ArrayList<String>();
        list1.add("1");
        boolean b = menuManagerService.deleteMenuClassBySId(list1);
        return JSON.toJSONString(b);
    }

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
    @RequestMapping("recommendListToApp")
    public String recommendListToApp(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        List<MenuInfoEntity> page = menuService.selectRecommendList(map);
        return JSON.toJSONString(ResultUtil.success(page));
    }
}
