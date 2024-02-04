package com.lineying.mapper;

import com.lineying.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {

    /**
     * 用户Id密码登录
     * @param entity
     * @return
     */
    List<Map<String, Object>> loginForUserId(LoginEntity entity);

    /**
     * 用户密码登录
     * @param entity
     * @return
     */
    List<Map<String, Object>> loginForUsername(LoginEntity entity);

    /**
     * 邮箱密码登录
     * @param entity
     * @return
     */
    List<Map<String, Object>> loginForEmail(LoginEntity entity);

    /**
     * Apple登录
     * @param entity
     * @return
     */
    List<Map<String, Object>> loginForApple(LoginEntity entity);

    /**
     * 查询
     *
     * @return
     */
    List<Map<String, Object>> list(CommonQueryEntity entity);

    /**
     * 新增
     *
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
