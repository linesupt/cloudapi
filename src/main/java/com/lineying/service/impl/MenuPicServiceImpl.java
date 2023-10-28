package com.lineying.service.impl;

import com.lineying.mapper.MenuPicMapper;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.service.IMenuPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据查询的具体实现
 * 菜单图片查询
 */
@Service
public class MenuPicServiceImpl implements IMenuPicService {

    @Autowired
    private MenuPicMapper menuPicMapper;

    @Override
    public List<MenuInfoEntity> selectPicNoload() {
        // TODO Auto-generated method stub
        return menuPicMapper.selectPicNoload();
    }

    @Override
    public int updateMenuPicStatus(MenuInfoEntity entity) {
        // TODO Auto-generated method stub
        return menuPicMapper.updateMenuPicStatus(entity);
    }

    @Override
    public MenuInfoEntity selectMenuInfoByid(String mid) {
        return menuPicMapper.selectMenuInfoByid(mid);
    }

    @Override
    public List<String> selectPicDownloadIsFail() {
        // TODO Auto-generated method stub
        return menuPicMapper.selectPicDownloadIsFail();
    }

    @Override
    public int updateMenuProcessById(MenuInfoEntity entity) {
        // TODO Auto-generated method stub
        return menuPicMapper.updateMenuProcessById(entity);
    }

}
