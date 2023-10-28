package com.lineying.service;

import java.util.List;
import java.util.Map;

import com.lineying.entity.MenuClassEntity;
import com.github.pagehelper.PageInfo;

/**
 * 菜单分类查询接口
 */
public interface IMenuClassService {
    /**
     * 新增菜单类型
     *
     * @param list
     * @return
     */
    int addMenuClass(List<MenuClassEntity> list);

    /**
     * 查询菜单类型分页
     *
     * @return
     */
    PageInfo<MenuClassEntity> selectMenuClassPage(Map<String, String> map);

    /**
     * 查询菜单类型所有
     *
     * @return
     */
    List<MenuClassEntity> selectAllMenuClass();

    /**
     * 更新菜单分类根据id
     *
     * @param entity
     * @return
     */
    int updateMenuClassById(MenuClassEntity entity);

}
