package com.lihuia.mysterious.service.service.jmx.sample.java.impl;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.java.JavaMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaParamVO;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaVO;
import com.lihuia.mysterious.service.service.jmx.sample.java.IJavaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
        List<JavaDO> javaDOList = javaMapper.getByJmxId(jmxId);
        if (CollectionUtils.isEmpty(javaDOList)) {
            return null;
        }
        // 假设第一个JavaDO对象包含了所有需要的信息
        JavaDO firstJavaDO = javaDOList.get(0);
        JavaVO javaVO = new JavaVO();
        javaVO.setTestCaseId(firstJavaDO.getTestCaseId());
        javaVO.setJmxId(firstJavaDO.getJmxId());
        javaVO.setJavaRequestClassPath(firstJavaDO.getJavaRequestClassPath());

        List<JavaParamVO> javaParamVOList = new ArrayList<>();
        for (JavaDO javaDO : javaDOList) {
            JavaParamVO javaParamVO = new JavaParamVO();
            javaParamVO.setParamKey(javaDO.getParamKey());
            javaParamVO.setParamValue(javaDO.getParamValue());
            javaParamVOList.add(javaParamVO);
        }
        javaVO.setJavaParamVOList(javaParamVOList);
        return javaVO;
    }

    @Override
    public void addJava(JavaVO javaVO) {
        Long testCaseId = javaVO.getTestCaseId();
        Long jmxId = javaVO.getJmxId();
        String javaRequestClassPath = javaVO.getJavaRequestClassPath();
        List<JavaParamVO> javaParamVOList = javaVO.getJavaParamVOList();
        if (CollectionUtils.isEmpty(javaParamVOList) || StringUtils.isBlank(javaRequestClassPath)) {
            throw new MysteriousException("ClassPath或者参数为空");
        }
        for (JavaParamVO javaParamVO : javaParamVOList) {
            JavaDO javaDO = new JavaDO();
            javaDO.setParamKey(javaParamVO.getParamKey());
            javaDO.setParamValue(javaParamVO.getParamValue());
            javaDO.setJavaRequestClassPath(javaRequestClassPath);
            javaDO.setTestCaseId(testCaseId);
            javaDO.setJmxId(jmxId);
            javaMapper.add(javaDO);
        }
    }

    @Override
    public void deleteJava(Long id) {
        javaMapper.delete(id);
    }

    @Override
    public void deleteByJmxId(Long jmxId) {
        javaMapper.deleteByJmxId(jmxId);
    }


    @Override
    public void updateJava(JavaVO javaVO) {
        Long testCaseId = javaVO.getTestCaseId();
        Long jmxId = javaVO.getJmxId();
        String javaRequestClassPath = javaVO.getJavaRequestClassPath();
        List<JavaParamVO> javaParamVOList = javaVO.getJavaParamVOList();
        if (CollectionUtils.isEmpty(javaParamVOList) || StringUtils.isBlank(javaRequestClassPath)) {
            throw new MysteriousException("ClassPath或者参数为空");
        }
        for (JavaParamVO javaParamVO : javaParamVOList) {
            JavaDO javaDO = new JavaDO();
            javaDO.setParamKey(javaParamVO.getParamKey());
            javaDO.setParamValue(javaParamVO.getParamValue());
            javaDO.setJavaRequestClassPath(javaRequestClassPath);
            javaDO.setTestCaseId(testCaseId);
            javaDO.setJmxId(jmxId);
            javaDO.setId(javaVO.getId());
            javaMapper.update(javaDO);
        }
    }
}
