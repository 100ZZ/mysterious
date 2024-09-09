package com.lihuia.mysterious.service.service.jmx.sample.java.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.java.JavaMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaVO;
import com.lihuia.mysterious.service.service.jmx.sample.java.IJavaService;
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
    public JavaVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(javaMapper.getByJmxId(jmxId), JavaVO.class);
    }

    @Override
    public void addJava(JavaVO javaVO) {
        javaMapper.add(BeanConverter.doSingle(javaVO, JavaDO.class));
    }

    @Override
    public void deleteJava(Long id) {
        javaMapper.delete(id);
    }

    @Override
    public void updateJava(JavaVO javaDO) {
        javaMapper.update(BeanConverter.doSingle(javaDO, JavaDO.class));
    }
}
