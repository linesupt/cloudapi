package com.lineying.mapper;

import com.lineying.entity.MenuInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuPicMapper {

    /**
     * @return
     */
    public List<MenuInfoEntity> selectPicNoload();

    /**
     * @param entity
     * @return
     */
    int updateMenuPicStatus(MenuInfoEntity entity);

    /**
     * @return
     */
    List<String> selectPicDownloadIsFail();

    /**
     * @param mid
     * @return
     */
    MenuInfoEntity selectMenuInfoByid(String mid);

    /**
     * @param entity
     * @return
     */
    int updateMenuProcessById(MenuInfoEntity entity);
}
