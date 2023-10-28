package com.lineying.mapper;

import java.util.List;
import java.util.Map;

import com.lineying.entity.FoodNutritionInfo;
import com.lineying.entity.MaterialUnits;
import com.lineying.entity.NutritionCompareEntity;
import com.lineying.entity.NutritionHealthAppraiseInfo;
import com.lineying.entity.NutritionIngredientEntity;
import com.lineying.entity.NutritionLightsEntity;
import org.springframework.stereotype.Component;

@Component
public interface FoodNutritionMapper {
    /**
     * ********************************************************
     *
     * @Title: addFoodNutrition
     * @Description: 新增营养价值
     * @Param: @param entity
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addFoodNutrition(FoodNutritionInfo entity);

    /**
     * ********************************************************
     *
     * @Title: addNutritionLights
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Param: @param list
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addNutritionLights(NutritionLightsEntity entity);

    /**
     * ********************************************************
     *
     * @Title: addNutritionIngredient
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Param: @param list
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addNutritionIngredient(NutritionIngredientEntity entity);

    /**
     * ********************************************************
     *
     * @Title: addNutritionHealthAppraiseInfo
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Param: @param list
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addNutritionHealthAppraiseInfo(List<NutritionHealthAppraiseInfo> list);

    /**
     * ********************************************************
     *
     * @Title: addNutritionUnits
     * @Description:
     * @Param: @param list
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addNutritionUnits(List<MaterialUnits> list);

    /**
     * ********************************************************
     *
     * @Title: addNutritionCompare
     * @Description:
     * @Param: @param entity
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addNutritionCompare(NutritionCompareEntity entity);

    /**
     * ********************************************************
     *
     * @Title: addServiceProvide
     * @Description:保存服务提供方数据
     * @Param: @param map
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addServiceProvide(Map<String, Object> map);

    /**
     * ********************************************************
     *
     * @Title: selectIsExist
     * @Description:查询是否存在
     * @Param: @param code
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int selectIsExist(String code);

}
