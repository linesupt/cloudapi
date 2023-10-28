package com.lineying.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineying.common.CommonConstant;
import com.lineying.mapper.MenuMapper;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.entity.MenuMaterialEntity;
import com.lineying.entity.MenuProcessEntity;
import com.lineying.service.IMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 数据查询的具体实现
 * 菜单数据查询
 *
 * @author ganjing
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Transactional
    @Override
    public boolean addMenuInfo(MenuInfoEntity entity) {
        boolean flag = menuMapper.addMenuInfo(entity) == 0 ? false : true;
        if (flag) {
            addMenuMaterial(entity.getMaterialList());
            addMenuProcess(entity.getProcessList());
        }
        return true;
    }

    @Override
    public int addMenuMaterial(List<MenuMaterialEntity> list) {
        return menuMapper.addMenuMaterial(list);
    }

    @Override
    public int addMenuProcess(List<MenuProcessEntity> list) {
        return menuMapper.addMenuProcess(list);
    }

    @Override
    public MenuInfoEntity selectMenuDetailById(String id) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("pic", CommonConstant.PIC_PATH);
        MenuInfoEntity menu = menuMapper.selectMenuDetailById(param);
        List<MenuProcessEntity> processList = menuMapper.selectProcessDetailById(param);
        List<MenuMaterialEntity> materialList = menuMapper.selectMaterialDetailById(id);
        menu.setProcessList(processList);
        menu.setMaterialList(materialList);
        return menu;
    }

    /**
     * **************************************************
     *
     * @Title: selectRecommendList
     * @Description: 查询推荐列表
     * @Param:@param map
     * @Param:@return
     * @see com.lineying.service.IMenuService#selectRecommendList(java.util.Map)
     * ***************************************************
     */
    @Override
    public PageInfo<MenuInfoEntity> selectRecommendListPage(Map<String, String> map) {
        int pageNum = 1;
        int pageSize = 1000;
        if (null != map) {
            pageNum = Integer.parseInt(map.get("pageNum") == null ? "1" : map.get("pageNum").toString());
            pageSize = Integer.parseInt(map.get("pageSize") == null ? "1000" : map.get("pageSize").toString());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MenuInfoEntity> list = menuMapper.selectRecommendListPage();
        PageInfo<MenuInfoEntity> page = new PageInfo<MenuInfoEntity>(list);
        return page;
    }

    /**
     * **************************************************
     *
     * @Title: updateRecommend
     * @Description: 修改推荐列表
     * @Param:@param obj
     * @Param:@return
     * @see com.lineying.service.IMenuService#updateRecommend(java.util.Map)
     * ***************************************************
     */
    @Override
    public boolean updateRecommend(Map<String, Object> obj) {
        boolean b = menuMapper.updateRecommend(obj) == 0 ? false : true;
        return b;
    }

    /**
     * **************************************************
     *
     * @Title: selectNoRecommendList
     * @Description:
     * @Param:@param obj
     * @Param:@return
     * @see com.lineying.service.IMenuService#selectNoRecommendList(java.util.Map)
     * ***************************************************
     */
    @Override
    public PageInfo<MenuInfoEntity> selectNoRecommendList(Map<String, String> map) {
        int pageNum = 1;
        int pageSize = 1000;
        if (null != map) {
            pageNum = Integer.parseInt(map.get("pageNum") == null ? "1" : map.get("pageNum").toString());
            pageSize = Integer.parseInt(map.get("pageSize") == null ? "1000" : map.get("pageSize").toString());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MenuInfoEntity> list = menuMapper.selectNoRecommendList(map);
        PageInfo<MenuInfoEntity> page = new PageInfo<MenuInfoEntity>(list);
        return page;
    }

    /**
     * **************************************************
     *
     * @Title: selectRecommendList
     * @Description: 推荐列表
     * @Param:@param map
     * @Param:@return
     * @see com.lineying.service.IMenuService#selectRecommendList(java.util.Map)
     * ***************************************************
     */
    @Override
    public List<MenuInfoEntity> selectRecommendList(Map<String, String> map) {
        map.put("pic", CommonConstant.PIC_PATH);
        List<MenuInfoEntity> list = menuMapper.selectRecommendList(map);
        for (int i = 0; i < list.size(); i++) {
            MenuInfoEntity entity = list.get(i);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", entity.getSid());
            param.put("pic", CommonConstant.PIC_PATH);
            List<MenuProcessEntity> processList = menuMapper.selectProcessDetailById(param);
            List<MenuMaterialEntity> materialList = menuMapper.selectMaterialDetailById(entity.getSid());
            entity.setProcessList(processList);
            entity.setMaterialList(materialList);
            list.set(i, entity);
        }
        return list;
    }


}
