package com.lineying.service.impl;

import com.lineying.entity.VerifyCodeEntity;
import com.lineying.mapper.VerifyCodeMapper;
import com.lineying.service.IVerifyCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 验证码数据操作
 */
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Resource
    VerifyCodeMapper verifyCodeMapper;

    @Override
    public boolean add(VerifyCodeEntity entity) {
        return verifyCodeMapper.add(entity);
    }

    @Override
    public List<Map<String, Object>> list(VerifyCodeEntity entity) {
        return verifyCodeMapper.list(entity);
    }

}
