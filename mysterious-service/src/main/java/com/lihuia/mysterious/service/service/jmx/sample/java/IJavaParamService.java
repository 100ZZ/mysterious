package com.lihuia.mysterious.service.service.jmx.sample.java;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IJavaParamService {

    /**
     * 新增java参数
     * @param javaParamDO
     */
    void addJavaParam(JavaParamDO javaParamDO);

    /**
     * 批量新增
     * @param javaParamDOList
     */
    void batchAddJavaParam(List<JavaParamDO> javaParamDOList);

    /**
     * 根据java sample获取所有的param参数
     * @param javaId
     * @return
     */
    List<JavaParamDO> getListByJavaId(Long javaId);

    /**
     * 根据jmxId获取param
     * @param jmxId
     * @return
     */
    List<JavaParamDO> getListByJmxId(Long jmxId);

    /**
     * 更新java参数
     * @param javaParamDO
     */
    void updateJavaParam(JavaParamDO javaParamDO);

    /**
     * 批量更新
     * @param javaParamDOList
     */
    void batchUpdateJavaParam(List<JavaParamDO> javaParamDOList);

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
    List<JavaParamDO> getExistParamList(Long javaId, String name);
}
