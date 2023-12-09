package com.lineying.service;

import com.lineying.entity.VerifyCodeEntity;

import java.util.List;
import java.util.Map;

/**
 * 验证服务
 */
public interface IVerifyCodeService {


    /**
     * 记录验证码
     * @param entity
     * @return
     */
    boolean add(VerifyCodeEntity entity);

    /**
     * 查询验证
     * @param entity
     * @return
     */
    List<Map<String, Object>> list(VerifyCodeEntity entity);


}
