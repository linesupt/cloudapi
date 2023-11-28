package com.lineying.mapper;

import com.lineying.entity.CommonQueryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
    /**
     * 查询
     * @return
     */
    List<Map<String, Object>> list(CommonQueryEntity entity);
}
