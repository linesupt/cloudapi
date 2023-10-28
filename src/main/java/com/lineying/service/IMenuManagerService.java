package com.lineying.service;

import com.lineying.entity.MenuInfoEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ganjing
 * @ClassName: IMenuManagerService
 * @Description: 菜单管理服务接口
 * @date 2019年3月6日 下午2:02:07
 */
public interface IMenuManagerService {
    /**
     * @param @param  info
     * @param @return
     * @return PageInfo<MenuInfoEntity>
     * @throws
     * @Title: selectMenuPage
     * @Description: 查询菜单分页
     * @auth ganjing
     */
    PageInfo<MenuInfoEntity> selectMenuPage(int pageNum, int pageSize,
                                            MenuInfoEntity info);

    /**
     * @param @param  entity
     * @param @return
     * @return int
     * @throws
     * @Title: updateMenuInfoBySId
     * @Description: 根据菜单sid更新类型
     * @auth ganjing
     */
    boolean updateMenuInfoBySId(MenuInfoEntity entity);

    /**
     * @param @param  id
     * @param @return
     * @return boolean
     * @throws
     * @Title: deleteMenuBySId
     * @Description: 删除菜谱根据sid
     * @auth ganjing
     */
    boolean deleteMenuBySId(List<String> id);

    /**
     * @param @param  id
     * @param @return
     * @return boolean
     * @throws
     * @Title: deleteMenuClassBySId
     * @Description: 删除菜谱类型根据sid
     * @auth ganjing
     */
    boolean deleteMenuClassBySId(List<String> id);
}
