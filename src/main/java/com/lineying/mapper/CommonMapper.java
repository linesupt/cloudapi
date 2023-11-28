package com.lineying.mapper;

import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
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
}
