package com.lineying.mapper;

import java.util.List;

import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MaterialMainClass;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 食材分類
 */
@Mapper
public interface MaterialClassMapper {
    /**
     * ********************************************************
     *
     * @Title: addMaterialClass
     * @Description:新增食材分類
     * @Param: @param entity
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    int addMaterialClass(MaterialClassEntity entity);

    /**
     * ********************************************************
     *
     * @Title: selectMaterialMainClass
     * @Description: 查询食材主分类
     * @Param: @return
     * @Return: List<MaterialMainClass>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    List<MaterialMainClass> selectMaterialMainClass();

    /**
     * ********************************************************
     *
     * @Title: selectMaterialPage
     * @Description: 查询食材分页
     * @Param: @param param
     * @Param: @return
     * @Return: List<MaterialClassEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    List<MaterialClassEntity> selectMaterialPage(Map<String, String> param);
}
