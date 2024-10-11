package com.lihuia.mysterious.service.service.jmx.sample.java;

import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaVO;

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
    JavaVO getByJmxId(Long jmxId);

    /**
     * 新增java request
     * @param javaVO
     */
    void addJava(JavaVO javaVO);

    /**
     * 删除java
     * @param id
     */
    void deleteJava(Long id);

    /**
     * 删除jmx脚本的java请求
     * @param jmxId
     */
    void deleteByJmxId(Long jmxId);

    /**
     * 更新java请求
     * @param javaVO
     */
    void updateJava(JavaVO javaVO);
}
