package com.lineying.mapper;

import com.lineying.entity.VerifyCodeEntity;

import java.util.List;
import java.util.Map;

/**
 * 验证码
 */
public interface VerifyCodeMapper {

    /**
     * 查询
     *
     * @return
     */
    List<Map<String, Object>> list(VerifyCodeEntity entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean add(VerifyCodeEntity entity);

}
