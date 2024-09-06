package com.lihuia.mysterious.service.service.jmx.sample.java.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class JavaService implements IJavaService {

    @Autowired
    private JavaMapper javaMapper;

    @Override
    public JavaDO getByJmxId(Long jmxId) {
        return javaMapper.getByJmxId(jmxId);
    }

    @Override
    public void addJava(JavaDO javaDO) {
        javaMapper.add(javaDO);
    }

    @Override
    public void deleteJava(Long id) {
        javaMapper.delete(id);
    }

    @Override
    public void updateJava(JavaDO javaDO) {
        javaMapper.update(javaDO);
    }
}
