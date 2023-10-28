package com.lineying.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lineying.mapper.MaterialClassMapper;
import com.lineying.entity.MaterialClassEntity;
import com.lineying.entity.MaterialMainClass;
import com.lineying.service.IMaterialClassService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * *********************************************
 *
 * @ClassName: MaterialClassServiceImpl
 * @Description:食材分类实现类
 * @Author ganjing
 * @Date 2019年4月27日
 * @Copyright: 2019 天誉飞歌(重庆)研究院版权所有
 * *********************************************
 */
@Service
public class MaterialClassServiceImpl implements IMaterialClassService {
    @Autowired
    private MaterialClassMapper materialClassMapper;

    /**
     * **************************************************
     *
     * @Title: addMaterialClass
     * @Description:
     * @Param:@param entity
     * @Param:@return
     * @see com.lineying.service.IMaterialClassService#addMaterialClass(com.lineying.entity.MaterialClassEntity)
     * ***************************************************
     */
    @Override
    public boolean addMaterialClass(MaterialClassEntity entity) {
        return false;
    }

    /**
     * **************************************************
     *
     * @Title: selectMaterialMainClass
     * @Description: 查询食材主分类
     * @Param:@return
     * @see com.lineying.service.IMaterialClassService#selectMaterialMainClass()
     * ***************************************************
     */
    @Override
    public List<MaterialMainClass> selectMaterialMainClass() {
        return materialClassMapper.selectMaterialMainClass();
    }

    /**
     * **************************************************
     *
     * @Title: selectMaterialPage
     * @Description: 查询食材分页
     * @Param:@param param
     * @Param:@return
     * @see com.lineying.service.IMaterialClassService#selectMaterialPage(java.util.Map)
     * ***************************************************
     */
    @Override
    public PageInfo<MaterialClassEntity> selectMaterialPage(Map<String, String> param) {
        int pageNum = Integer.parseInt(param.get("pageNum") == null ? "1" : param.get("pageNum").toString());
        int pageSize = Integer.parseInt(param.get("pageSize") == null ? "1000" : param.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);
        List<MaterialClassEntity> list = materialClassMapper.selectMaterialPage(param);
        PageInfo<MaterialClassEntity> page = new PageInfo<MaterialClassEntity>(list);
        return page;
    }
}
