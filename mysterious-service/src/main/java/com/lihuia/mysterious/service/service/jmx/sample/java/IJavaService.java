package com.lihuia.mysterious.service.service.jmx.sample.java;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */


public interface IJavaService {

    /**
     * 根据jmxId获取java sample
     * @param jmxId
     * @return
     */
    JavaDO getByJmxId(Long jmxId);

    /**
     * 新增java request
     * @param javaDO
     */
    void addJava(JavaDO javaDO);

    /**
     * 删除java
     * @param id
     */
    void deleteJava(Long id);

    /**
     * 更新java请求
     * @param javaDO
     */
    void updateJava(JavaDO javaDO);
}
