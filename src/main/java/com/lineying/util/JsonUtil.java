package com.lineying.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lineying.entity.FoodNutritionInfo;
import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MenuClassEntity;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.entity.MenuMaterialEntity;
import com.lineying.entity.MenuProcessEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * json对象
 *
 * @author ganjing
 */
public class JsonUtil {
    /**
     * 得到菜单信息
     *
     * @param data
     * @return
     */
    public static List<MenuInfoEntity> getMenuInfoList(String data) {
        if (null == data) {
            return null;
        }
        List<MenuInfoEntity> menuList = new ArrayList<MenuInfoEntity>();
        try {
            JSONObject obj = JSON.parseObject(data);
            if (!"0".equals(obj.get("status"))) {
                return null;
            }
            JSONObject result = JSON.parseObject(obj.get("result").toString());
            JSONArray list = JSON.parseArray(result.get("list").toString());
            for (int i = 0; i < list.size(); i++) {
                //MenuInfoEntity entity = new MenuInfoEntity();
                MenuInfoEntity entity = JSON.parseObject(list.get(i).toString(), MenuInfoEntity.class);
                //下载菜单图片
                boolean b = HttpClientUtil.downloadImage(entity.getPic(),
                        "menuImage" + File.separator + entity.getclassId() + File.separator + entity.getId());
                if (b) {
                    entity.setPic_isEmpty("1");
                } else {
                    entity.setPic_isEmpty("0");
                }
                entity.setSid(JSON.parseObject(list.get(i).toString()).get("id").toString());
                Object processJson = JSON.parseObject(list.get(i).toString()).get("process");
                Object materialJson = JSON.parseObject(list.get(i).toString()).get("material");
                List<MenuProcessEntity> processList = JSON.parseArray(processJson.toString(), MenuProcessEntity.class);
                List<MenuMaterialEntity> materialList = JSON.parseArray(materialJson.toString(), MenuMaterialEntity.class);
                //循环
                for (int m = 0; m < processList.size(); m++) {
                    processList.get(m).setMid(entity.getId());
                    boolean c = HttpClientUtil.downloadImage(processList.get(m).getPic(),
                            "menuImage" + File.separator + entity.getclassId() + File.separator + entity.getId());
                    if (c) {
                        processList.get(m).setPic_isEmpty("1");
                    } else {
                        processList.get(m).setPic_isEmpty("0");
                    }
                }
                for (int n = 0; n < materialList.size(); n++) {
                    materialList.get(n).setMid(entity.getId());
                }
                entity.setProcessList(processList);
                entity.setMaterialList(materialList);
                menuList.add(entity);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return menuList;
    }

    /**
     * ********************************************************
     *
     * @Title: getMaterialClass
     * @Description: 得到规格分类
     * @Param: @param data
     * @Param: @return
     * @Return: List<MaterialClassEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    public static List<MaterialClassEntity> getMaterialClass(String data) {
        JSONObject obj = JSON.parseObject(data);
        List<MaterialClassEntity> list = JSON.parseArray(obj.get("foods").toString(), MaterialClassEntity.class);
        return list;
    }

    /**
     * ********************************************************
     *
     * @Title: getFoodNutrition
     * @Description:得到食物规格
     * @Param: @param data
     * @Param: @return
     * @Return: FoodNutritionInfo
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    public static FoodNutritionInfo getFoodNutrition(String data) {
        FoodNutritionInfo entity = JSON.parseObject(data, FoodNutritionInfo.class);
        return entity;
    }

    public static List<MenuClassEntity> getMenuClassList(String data) {
        JSONObject obj = JSON.parseObject(data);
        JSONArray list = JSON.parseArray(obj.get("result").toString());
        List<MenuClassEntity> allClassList = new ArrayList<MenuClassEntity>();
        for (int i = 0; i < list.size(); i++) {
            //一级分类
            String classId = JSON.parseObject(list.get(i).toString()).get("classId").toString();
            String name = JSON.parseObject(list.get(i).toString()).get("name").toString();
            String parentId = JSON.parseObject(list.get(i).toString()).get("parentid").toString();
            MenuClassEntity parentEntity = new MenuClassEntity();
            parentEntity.setclassId(classId);
            parentEntity.setName(name);
            parentEntity.setParentid(parentId);
            allClassList.add(parentEntity);
            //下载以及
            //二级分类
            String jsonStr = JSON.parseObject(list.get(i).toString()).get("list").toString();
            List<MenuClassEntity> classList = JSON.parseArray(jsonStr, MenuClassEntity.class);
            allClassList.addAll(classList);
        }
        return allClassList;
    }
}
