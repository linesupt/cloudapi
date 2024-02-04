package com.lineying.service.impl;

import com.lineying.entity.*;
import com.lineying.mapper.CommonMapper;
import com.lineying.service.ICommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements ICommonService {
    @Resource
    CommonMapper commonMapper;

    /**
     * 用户id密码登录
     * @param entity
     * @return
     */
    @Override
    public List<Map<String, Object>> loginForUserId(LoginEntity entity) {
        return commonMapper.loginForUserId(entity);
    }

    /**
     * 用户名密码登录
     * @param entity
     * @return
     */
    @Override
    public List<Map<String, Object>> loginForUsername(LoginEntity entity) {
        return commonMapper.loginForUsername(entity);
    }

    /**
     * 邮箱密码登录
     * @param entity
     * @return
     */
    @Override
    public List<Map<String, Object>> loginForEmail(LoginEntity entity) {
        return commonMapper.loginForEmail(entity);
    }

    /**
     * Apple登录
     * @param entity
     * @return
     */
    @Override
    public List<Map<String, Object>> loginForApple(LoginEntity entity) {
        return commonMapper.loginForApple(entity);
    }

    /**
     * 查询
     * @param entity
     * @return
     */
    @Override
    public List<Map<String, Object>> list(CommonQueryEntity entity) {
        return commonMapper.list(entity);
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @Override
    public boolean add(CommonAddEntity entity) {
        return commonMapper.add(entity);
    }

    /**
     * 删除
     * @param entity
     * @return
     */
    @Override
    public boolean delete(CommonQueryEntity entity) {
        return commonMapper.delete(entity);
    }

    /**
     * 更新
     * @param entity
     * @return
     */
    @Override
    public boolean update(CommonUpdateEntity entity) {
        return commonMapper.update(entity);
    }

    @Override
    public boolean command(CommonCommandEntity entity) {
        return commonMapper.command(entity);
    }
}
