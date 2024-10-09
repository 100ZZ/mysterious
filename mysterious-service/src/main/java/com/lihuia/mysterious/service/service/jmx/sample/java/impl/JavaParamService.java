package com.lihuia.mysterious.service.service.jmx.sample.java.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaParamDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.java.JavaParamMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaParamVO;
import com.lihuia.mysterious.service.service.jmx.sample.java.IJavaParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class JavaParamService implements IJavaParamService {

    @Autowired
    private JavaParamMapper javaParamMapper;

    @Override
    public void addJavaParam(JavaParamVO javaParamVO) {
        javaParamMapper.add(BeanConverter.doSingle(javaParamVO, JavaParamDO.class));
    }

    @Override
    public void batchAddJavaParam(List<JavaParamVO> javaParamVOList) {
        javaParamMapper.batchAdd(BeanConverter.doList(javaParamVOList, JavaParamDO.class));
    }

    @Override
    public List<JavaParamVO> getListByJavaId(Long javaId) {
        return BeanConverter.doList(javaParamMapper.getListByJavaId(javaId), JavaParamVO.class);
    }

    @Override
    public List<JavaParamVO> getListByJmxId(Long jmxId) {
        return BeanConverter.doList(javaParamMapper.getListByJmxId(jmxId), JavaParamVO.class);
    }

    @Override
    public void updateJavaParam(JavaParamVO javaParamVO) {
        javaParamMapper.update(BeanConverter.doSingle(javaParamVO, JavaParamDO.class));
    }

    @Override
    public void batchUpdateJavaParam(List<JavaParamVO> javaParamVOList) {
        javaParamMapper.batchUpdate(BeanConverter.doList(javaParamVOList, JavaParamDO.class));
    }

    @Override
    public void deleteJavaParam(Long id) {
        javaParamMapper.delete(id);
    }

    @Override
    public void batchDeleteJavaParam(List<Long> ids) {
        javaParamMapper.batchDelete(ids);
    }

    @Override
    public List<JavaParamVO> getExistParamList(Long javaId, String paramKey) {
        return BeanConverter.doList(javaParamMapper.getExistParamList(javaId, paramKey), JavaParamVO.class);
    }
}
