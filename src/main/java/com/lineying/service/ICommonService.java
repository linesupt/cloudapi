package com.lineying.service;

import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;

import java.util.List;
import java.util.Map;

public interface ICommonService {
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
}
