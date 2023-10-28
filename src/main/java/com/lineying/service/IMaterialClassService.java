package com.lineying.service;

import java.util.List;
import java.util.Map;

import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MaterialMainClass;
import com.github.pagehelper.PageInfo;

public interface IMaterialClassService {
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
    boolean addMaterialClass(MaterialClassEntity entity);

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
    PageInfo<MaterialClassEntity> selectMaterialPage(Map<String, String> param);
}
