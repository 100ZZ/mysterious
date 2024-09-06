package com.lihuia.mysterious.service.service.jmx.sample.java.impl;

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
    public void addJavaParam(JavaParamDO javaParamDO) {
        javaParamMapper.add(javaParamDO);
    }

    @Override
    public void batchAddJavaParam(List<JavaParamDO> javaParamDOList) {
        javaParamMapper.batchAdd(javaParamDOList);
    }

    @Override
    public List<JavaParamDO> getListByJavaId(Long javaId) {
        return javaParamMapper.getListByJavaId(javaId);
    }

    @Override
    public List<JavaParamDO> getListByJmxId(Long jmxId) {
        return javaParamMapper.getListByJmxId(jmxId);
    }

    @Override
    public void updateJavaParam(JavaParamDO javaParamDO) {
        javaParamMapper.update(javaParamDO);
    }

    @Override
    public void batchUpdateJavaParam(List<JavaParamDO> javaParamDOList) {
        javaParamMapper.batchUpdate(javaParamDOList);
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
    public List<JavaParamDO> getExistParamList(Long javaId, String name) {
        return javaParamMapper.getExistParamList(javaId, name);
    }
}
