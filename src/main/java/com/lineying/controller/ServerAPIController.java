package com.lineying.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.lineying.common.CommonConstant;
import com.lineying.common.ResultUtil;
import com.lineying.entity.MenuClassEntity;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.service.IMenuClassService;
import com.lineying.service.IMenuService;
import com.lineying.util.HttpClientUtil;
import com.lineying.util.JsonUtil;
import com.lineying.util.SignUtil;

/**
 * 获取服务商的数据
 * 发布时不要公开这个接口，防止调用
 */
// @RestController
public class ServerAPIController {

    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuClassService menuClassService;

    /**
     * 先查询所有分类然后在查询id
     */
    @RequestMapping("addAllMenuInfo")
    public String addAllMenuInfo(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        //二级分类
        List<MenuClassEntity> menuList = menuClassService.selectAllMenuClass();
        //循环二级分类
        for (int i = 0; i < menuList.size(); i++) {
            int start = 0;
            String data = HttpClientUtil.get(CommonConstant.API_SERVER_BASE_URL + "/byclass?classId=" + menuList.get(i).getclassId() + "&start=" + start + "&num=20&appkey=" + CommonConstant.APPKEY);
            List<MenuInfoEntity> list = JsonUtil.getMenuInfoList(data);
            if (null == list || list.isEmpty()) {
                break;
            } else {
                for (MenuInfoEntity menuInfo : list) {
                    menuService.addMenuInfo(menuInfo);
                }
            }
        }
        return "ok";
    }

    /**
     * 添加菜单分类
     *
     * @param request
     * @return
     */
    @RequestMapping("addAllMenuClass")
    public String addAllMenuClass(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        String data = HttpClientUtil.get(CommonConstant.API_SERVER_BASE_URL + "/class?appkey=" + CommonConstant.APPKEY);
        //List<MenuClassEntity> list = JsonUtil.getMenuInfoList(data);
        List<MenuClassEntity> list = JsonUtil.getMenuClassList(data);
        menuClassService.addMenuClass(list);
        return "ok";
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("selectMenuClassPage")
    public String selectMenuClassPage(int pageNum, int pageSize) {
    	
    /*	System.out.println("pageNum:"+pageNum+",pageSize:"+pageSize);
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("pageNum", pageNum);
    	param.put("pageSize",pageSize);
    	PageInfo<MenuClassEntity> page = menuService.selectMenuClassPage(param);
    	//二级分类
    	List<MenuClassEntity> menuList = page.getList();
    	循环二级分类
    	for(int i = 0;i<menuList.size();i++) {*/
        int start = 0;
        while (true) {
            System.out.println("*****进入程序" + pageNum + "******");

            String data = HttpClientUtil.get(CommonConstant.API_SERVER_BASE_URL + "/byclass?classId=" + pageNum + "&start=" + start + "&num=20&appkey=449a9b6f2a5bda2f");
            List<MenuInfoEntity> list = JsonUtil.getMenuInfoList(data);
            if (null == list || list.isEmpty()) {
                System.out.println("===========end============");
                break;
            } else {
                for (MenuInfoEntity menuInfo : list) {
                    menuService.addMenuInfo(menuInfo);
                }
            }
            start = start + 20;
            System.out.println("正在现在类为" + pageNum + "起始记录为" + start + "的记录");
        }
        /*}*/
        return "ok" + pageNum;
    }
}