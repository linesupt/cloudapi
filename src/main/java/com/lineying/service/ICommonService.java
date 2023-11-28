package com.lineying.service;

import com.lineying.entity.CommonQueryEntity;

import java.util.List;
import java.util.Map;

public interface ICommonService {
    /**
     * 查询
     * @return
     */
    List<Map<String, Object>> list(CommonQueryEntity entity);


}
