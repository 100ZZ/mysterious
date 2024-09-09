package com.lihuia.mysterious.service.service.jmx.sample.java;

import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaParamVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IJavaParamService {

    /**
     * 新增java参数
     * @param javaParamVO
     */
    void addJavaParam(JavaParamVO javaParamVO);

    /**
     * 批量新增
     * @param javaParamVOList
     */
    void batchAddJavaParam(List<JavaParamVO> javaParamVOList);

    /**
     * 根据java sample获取所有的param参数
     * @param javaId
     * @return
     */
    List<JavaParamVO> getListByJavaId(Long javaId);

    /**
     * 根据jmxId获取param
     * @param jmxId
     * @return
     */
    List<JavaParamVO> getListByJmxId(Long jmxId);

    /**
     * 更新java参数
     * @param javaParamVO
     */
    void updateJavaParam(JavaParamVO javaParamVO);

    /**
     * 批量更新
     * @param javaParamVOList
     */
    void batchUpdateJavaParam(List<JavaParamVO> javaParamVOList);

    /**
     * 删除
     * @param id
     */
    void deleteJavaParam(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDeleteJavaParam(List<Long> ids);

    /**
     * 查找是否已存在
     * @param javaId
     * @param name
     * @return
     */
    List<JavaParamVO> getExistParamList(Long javaId, String name);
}
