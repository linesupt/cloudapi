package com.lineying.service;

import com.lineying.entity.*;

import java.util.List;
import java.util.Map;

public interface ICommonService {

    /**
     * 用户密码登录
     * @return
     */
    List<Map<String, Object>> loginForUsername(LoginEntity entity);

    /**
     * 邮箱密码登录
     * @return
     */
    List<Map<String, Object>> loginForEmail(LoginEntity entity);

    /**
     * 查询
     * @return
     */
    List<Map<String, Object>> list(CommonQueryEntity entity);

    /**
     * 新增
     * @param entity
     * @return
     */
    boolean add(CommonAddEntity entity);

    /**
     * 删除
     * @param entity
     * @return
     */
    boolean delete(CommonQueryEntity entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    boolean update(CommonUpdateEntity entity);

    /**
     * 执行原始sql
     * @param entity
     * @return
     */
    boolean command(CommonCommandEntity entity);

}
