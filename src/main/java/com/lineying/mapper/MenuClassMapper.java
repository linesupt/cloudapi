package com.lineying.mapper;

import com.lineying.entity.MenuClassEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 菜单分类
 */
@Component
public interface MenuClassMapper {

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
    List<MenuClassEntity> selectMenuClassPage(Map<String, String> map);

    /**
     * 更新菜单分类根据id
     *
     * @param entity
     * @return
     */
    int updateMenuClassById(MenuClassEntity entity);

}
