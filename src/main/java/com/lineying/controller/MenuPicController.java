package com.lineying.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.lineying.common.CommonConstant;
import com.lineying.common.ResultUtil;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.service.IMenuPicService;
import com.lineying.util.HttpClientUtil;
import com.lineying.util.HttpClientUtil1;
import com.lineying.util.SignUtil;

/**
 * 检查同步的图片数据
 */
//@RestController
public class MenuPicController {
    @Autowired
    private IMenuPicService menuPicService;

    /**
     * @param request
     * @return
     */
    @RequestMapping("selectAllPic")
    public String selectAllPic(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        try {
            List<MenuInfoEntity> menuList = menuPicService.selectPicNoload();
            for (MenuInfoEntity entity : menuList) {
                //下载菜单图片
                boolean b;
                try {
                    b = HttpClientUtil.downloadImage(entity.getPic(),
                            CommonConstant.IMAGE_PATH + File.separator + entity.getclassId() + File.separator + entity.getSid());
                    if (b) {
                        entity.setPic_isEmpty("1");
                    } else {
                        entity.setPic_isEmpty("0");
                    }
                    menuPicService.updateMenuPicStatus(entity);
                    //Thread.sleep(4000);
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping("selectAllPicIsEntity")
    public String selectAllPicIsEntity(HttpServletRequest request) {
        Map<String, String> map = SignUtil.getHttpParamMap(request);
        if (!SignUtil.validateSign(map)) {
            return JSON.toJSONString(ResultUtil.signError());
        }
        try {
            List<String> list = menuPicService.selectPicDownloadIsFail();
            for (String s : list) {
                MenuInfoEntity entity = menuPicService.selectMenuInfoByid(s);
                boolean flag;
                try {
                    flag = HttpClientUtil1.get(entity.getPic(),
                            CommonConstant.IMAGE_PATH + File.separator + entity.getclassId() + File.separator + entity.getSid());
                    if (flag) {
                        entity.setPic_isEmpty("1");
                    } else {
                        entity.setPic_isEmpty("0");
                    }
                    menuPicService.updateMenuProcessById(entity);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
