package com.lineying.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.mapper.FoodNutritionMapper;
import com.lineying.mapper.MaterialClassMapper;
import com.lineying.entity.FoodNutritionInfo;
import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MaterialUnits;
import com.lineying.entity.NutritionCompareEntity;
import com.lineying.entity.NutritionHealthAppraiseInfo;
import com.lineying.entity.NutritionIngredientEntity;
import com.lineying.entity.NutritionLightsEntity;
import com.lineying.service.IFoodNutritionService;
import com.lineying.util.HttpClientUtil;

@Service
public class FoodNutritionServiceImpl implements IFoodNutritionService {
    @Autowired
    private MaterialClassMapper materialClassMapper;
    @Autowired
    private FoodNutritionMapper foodNutritionMapper;

    @Override
    public boolean addFoodNutrition() {
        for (int i = 1; i < 12; i++) {
            for (int j = 1; j < 9999999; j++) {
                //
                String allClassData = HttpClientUtil.get("http://food.boohee.com/fb/v1/foods?kind=group&value=" + i + "&order_by=1&page=" + j + "&order_asc=0&token=&user_key=&app_version=2.6.2.1&app_device=Android&os_version=9&phone_model=MIX+2S&channel=tencent");
                if (null == allClassData) {
                    break;
                }
                JSONObject allObj = JSON.parseObject(allClassData);
                List<MaterialClassEntity> list = JSON.parseArray(allObj.get("foods").toString(), MaterialClassEntity.class);
                if (null == list) {
                    break;
                }
                //判断是否重复
                boolean b = foodNutritionMapper.selectIsExist(list.get(0).getCode()) == 0 ? false : true;
                if (b) {
                    break;
                }
                for (MaterialClassEntity entity : list) {
                    entity.setMain_class_id(i + "");
                    materialClassMapper.addMaterialClass(entity);
                    System.out.println(entity.getCode());
                    String nutritiondata = HttpClientUtil.get("http://food.boohee.com/fb/v1/foods/" + entity.getCode() + "/mode_show?token=&user_key=&app_version=2.6.2.1&app_device=Android&os_version=9&phone_model=MIX+2S&channel=tencent");
                    if (null == nutritiondata) {
                        continue;
                    }
                    JSONObject nutritionObj = JSON.parseObject(nutritiondata);
                    if (null == nutritionObj) {
                        continue;
                    }
                    FoodNutritionInfo nutritionInfo = JSON.parseObject(nutritiondata, FoodNutritionInfo.class);
                    if (null == nutritionInfo) {
                        continue;
                    }
                    String id = nutritionInfo.getId();
                    if (null == id) {
                        continue;
                    }
                    foodNutritionMapper.addFoodNutrition(nutritionInfo);
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("id", nutritionInfo.getId());
                    param.put("remark", nutritiondata);
                    foodNutritionMapper.addServiceProvide(param);

                    NutritionCompareEntity compare = JSON.parseObject(nutritionObj.get("compare").toString(), NutritionCompareEntity.class);
                    if (null != compare) {
                        compare.setId(id);
                        foodNutritionMapper.addNutritionCompare(compare);
                    }


                    NutritionLightsEntity lights = JSON.parseObject(nutritionObj.get("lights").toString(), NutritionLightsEntity.class);
                    if (null != lights) {
                        lights.setId(id);
                        foodNutritionMapper.addNutritionLights(lights);
                    }


                    List<MaterialUnits> utils = JSON.parseArray(nutritionObj.get("units").toString(), MaterialUnits.class);
                    if (null != utils && utils.size() > 0) {
                        for (MaterialUnits mu : utils) {
                            mu.setId(id);
                        }
                        foodNutritionMapper.addNutritionUnits(utils);
                    }


                    NutritionIngredientEntity ingredients = JSON.parseObject(nutritionObj.get("ingredient").toString(), NutritionIngredientEntity.class);
                    if (null != ingredients) {
                        ingredients.setId(id);
                        foodNutritionMapper.addNutritionIngredient(ingredients);
                    }

                    List<NutritionHealthAppraiseInfo> healthAppraise = JSON.parseArray(nutritionObj.get("health_appraise").toString(), NutritionHealthAppraiseInfo.class);
                    if (null != healthAppraise && healthAppraise.size() > 0) {
                        for (NutritionHealthAppraiseInfo nh : healthAppraise) {
                            nh.setId(id);
                        }
                        foodNutritionMapper.addNutritionHealthAppraiseInfo(healthAppraise);
                    }

                }
            }

        }


        return false;
    }

}
