package com.lineying.mapper;

import com.lineying.entity.VerifyCodeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 验证码
 */
@Mapper
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
