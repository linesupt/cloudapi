package com.lineying.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lineying.service.IFoodNutritionService;

/**
 * *********************************************
 *
 * @ClassName: FoodNutritionController
 * @Description:食物营养控制器
 * @Author ganjing
 * @Date 2019年4月20日
 * @Copyright: 2019 天誉飞歌(重庆)研究院版权所有
 * *********************************************
 */
@RestController
public class FoodNutritionController {
    @Autowired
    private IFoodNutritionService foodNutritionService;

    /**
     * ********************************************************
     *
     * @Title: selectMenuPage
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Param: @param request
     * @Param: @return
     * @Return: String
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    @RequestMapping("/addMaterial")
    public String addMaterial(HttpServletRequest request) {
        foodNutritionService.addFoodNutrition();
        return "";
    }
}
