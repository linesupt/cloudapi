package com.lineying.service;

import java.util.List;
import java.util.Map;

import com.lineying.entity.MenuInfoEntity;
import com.lineying.entity.MenuMaterialEntity;
import com.lineying.entity.MenuProcessEntity;
import com.github.pagehelper.PageInfo;

/**
 * 菜单服务
 */
public interface IMenuService {
    /**
     * 新增菜品
     *
     * @param entity
     * @return
     */
    boolean addMenuInfo(MenuInfoEntity entity);

    /**
     * 新增菜品处理流程
     *
     * @param list
     * @return
     */
    int addMenuMaterial(List<MenuMaterialEntity> list);

    /**
     * 新增菜品素材
     *
     * @param list
     * @return
     */
    int addMenuProcess(List<MenuProcessEntity> list);

    /**
     * 查询菜品详情
     *
     * @param id
     * @return
     */
    MenuInfoEntity selectMenuDetailById(String id);

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐菜谱
     * @Param: @return
     * @Return: Page<MenuInfoEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    List<MenuInfoEntity> selectRecommendList(Map<String, String> map);

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐菜谱
     * @Param: @return
     * @Return: Page<MenuInfoEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    PageInfo<MenuInfoEntity> selectRecommendListPage(Map<String, String> map);

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description:修改推荐菜谱
     * @Param: @return
     * @Return: int
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    boolean updateRecommend(Map<String, Object> obj);

    /**
     * ********************************************************
     *
     * @Title: selectNoRecommendList
     * @Description:查询未推荐列表
     * @Param: @param obj
     * @Param: @return
     * @Return: List<MenuInfoEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    PageInfo<MenuInfoEntity> selectNoRecommendList(Map<String, String> obj);

}
