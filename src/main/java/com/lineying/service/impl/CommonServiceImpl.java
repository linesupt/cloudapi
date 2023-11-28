package com.lineying.service.impl;

import com.lineying.entity.CommonQueryEntity;
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

    @Override
    public List<Map<String, Object>> list(CommonQueryEntity entity) {
        return commonMapper.list(entity);
    }
}
