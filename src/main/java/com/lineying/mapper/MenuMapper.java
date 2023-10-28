package com.lineying.mapper;

import com.lineying.entity.MenuInfoEntity;
import com.lineying.entity.MenuMaterialEntity;
import com.lineying.entity.MenuProcessEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 菜品信息访问
 */
@Component
public interface MenuMapper {

    /**
     * 新增菜譜
     *
     * @param entity
     * @return
     */
    int addMenuInfo(MenuInfoEntity entity);

    /**
     * 新增菜谱流程
     *
     * @param list
     * @return
     */
    int addMenuMaterial(List<MenuMaterialEntity> list);

    /**
     * 新增菜谱素材
     *
     * @param list
     * @return
     */
    int addMenuProcess(List<MenuProcessEntity> list);

    /**
     * 查询菜单详情根据id
     *
     * @param obj
     * @return
     */
    MenuInfoEntity selectMenuDetailById(Map<String, Object> obj);

    /**
     * 查询菜单图片详情
     *
     * @return
     */
    List<MenuProcessEntity> selectProcessDetailById(Map<String, Object> obj);

    /**
     * 查询菜单图片详情
     *
     * @param id
     * @return
     */
    List<MenuMaterialEntity> selectMaterialDetailById(String id);

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐菜谱
     * @Param: @return
     * @Return: List<MenuInfoEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    List<MenuInfoEntity> selectRecommendList(Map<String, String> obj);

    /**
     * ********************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐菜谱分页
     * @Param: @return
     * @Return: List<MenuInfoEntity>
     * @Throws
     * @Author ganjing
     * *********************************************************
     */
    List<MenuInfoEntity> selectRecommendListPage();

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
    int updateRecommend(Map<String, Object> obj);

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
    List<MenuInfoEntity> selectNoRecommendList(Map<String, String> obj);
}
