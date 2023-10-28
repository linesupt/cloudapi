package com.lineying.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lineying.mapper.MenuClassMapper;
import com.lineying.entity.MenuClassEntity;
import com.lineying.service.IMenuClassService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 数据查询的具体实现
 * 菜单分类查询
 */
@Service
public class MenuClassServiceImpl implements IMenuClassService {

    @Autowired
    private MenuClassMapper menuClassMapper;

    @Override
    public int addMenuClass(List<MenuClassEntity> list) {
        return menuClassMapper.addMenuClass(list);
    }

    @Override
    public PageInfo<MenuClassEntity> selectMenuClassPage(Map<String, String> map) {
        int pageNum = Integer.parseInt(map.get("pageNum") == null ? "1" : map.get("pageNum").toString());
        int pageSize = Integer.parseInt(map.get("pageSize") == null ? "1000" : map.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);
        List<MenuClassEntity> list = menuClassMapper.selectMenuClassPage(map);
        PageInfo<MenuClassEntity> page = new PageInfo<MenuClassEntity>(list);
        return page;
    }

    @Override
    public int updateMenuClassById(MenuClassEntity entity) {
        return menuClassMapper.updateMenuClassById(entity);
    }

    /**
     * **************************************************
     *
     * @Title: selectAllMenuClass
     * @Description: 查询所有菜谱分类
     * @Param:@return
     * @see com.lineying.service.IMenuClassService#selectAllMenuClass()
     * ***************************************************
     */
    @Override
    public List<MenuClassEntity> selectAllMenuClass() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("parentId", "0");
        List<MenuClassEntity> list = menuClassMapper.selectMenuClassPage(map);
        for (MenuClassEntity s : list) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("parentId", s.getclassId());
            List<MenuClassEntity> listResult = menuClassMapper.selectMenuClassPage(map);
            s.setList(listResult);
        }
        return list;
    }


}
