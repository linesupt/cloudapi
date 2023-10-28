package com.lineying.mapper;

import com.lineying.entity.MenuInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public interface MenuManagerMapper {

    /**
     * 查询
     *
     * @param entity
     * @return
     */
    List<MenuInfoEntity> selectMenuPage(MenuInfoEntity entity);

    /**
     * 根据菜单sid更新类型
     *
     * @param entity
     * @return
     */
    int updateMenuInfoBySId(MenuInfoEntity entity);

    /**
     * 删除菜谱根据sid
     *
     * @param id
     * @return
     */
    int deleteMenuBySId(List<String> id);

    /**
     * 删除菜谱类型根据sid
     *
     * @param id
     * @return
     */
    int deleteMenuClassBySId(List<String> id);
}
