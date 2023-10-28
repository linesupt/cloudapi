package com.lineying.service;

import com.lineying.entity.MenuInfoEntity;

import java.util.List;

/**
 * 图片查询接口
 */
public interface IMenuPicService {
    /**
     * @return
     */
    List<MenuInfoEntity> selectPicNoload();

    /**
     * @param entity
     * @return
     */
    int updateMenuPicStatus(MenuInfoEntity entity);

    /**
     * @param mid
     * @return
     */
    MenuInfoEntity selectMenuInfoByid(String mid);

    /**
     * @return
     */
    List<String> selectPicDownloadIsFail();

    /**
     * @param entity
     * @return
     */
    int updateMenuProcessById(MenuInfoEntity entity);
}
