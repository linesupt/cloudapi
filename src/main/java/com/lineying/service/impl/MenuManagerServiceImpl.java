package com.lineying.service.impl;

import com.lineying.common.CommonConstant;
import com.lineying.mapper.MenuMapper;
import com.lineying.mapper.MenuManagerMapper;
import com.lineying.entity.MenuInfoEntity;
import com.lineying.entity.MenuMaterialEntity;
import com.lineying.entity.MenuProcessEntity;
import com.lineying.service.IMenuManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据查询的具体实现
 * 菜单管理查询与菜单数据查询
 *
 * @author ganjing
 * @date 2019年3月6日 下午2:03:05
 */
@Service
public class MenuManagerServiceImpl implements IMenuManagerService {

    @Autowired
    private MenuManagerMapper menuManagerMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 查询菜单分页
     */
    @Override
    public PageInfo<MenuInfoEntity> selectMenuPage(int pageNum, int pageSize,
                                                   MenuInfoEntity info) {
        info.setPic(CommonConstant.PIC_PATH);
        PageHelper.startPage(pageNum, pageSize);
        List<MenuInfoEntity> list = menuManagerMapper.selectMenuPage(info);
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
        PageInfo<MenuInfoEntity> page = new PageInfo<MenuInfoEntity>(list);
        return page;
    }

    /**
     * 根据菜谱sid更新菜谱
     */
    @Override
    public boolean updateMenuInfoBySId(MenuInfoEntity entity) {
        return menuManagerMapper.updateMenuInfoBySId(entity) == 0 ? false : true;
    }

    /**
     * 删除菜谱根据sid
     */
    @Override
    public boolean deleteMenuBySId(List<String> id) {
        return menuManagerMapper.deleteMenuBySId(id) == 0 ? false : true;
    }

    /**
     * 删除菜谱类型根据sid
     */
    @Override
    public boolean deleteMenuClassBySId(List<String> id) {
        return menuManagerMapper.deleteMenuClassBySId(id) == 0 ? false : true;
    }

}
