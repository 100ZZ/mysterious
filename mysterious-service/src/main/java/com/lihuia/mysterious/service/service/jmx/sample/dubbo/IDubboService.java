package com.lihuia.mysterious.service.service.jmx.sample.dubbo;


import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboDO;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */


public interface IDubboService {

    /**
     * 根据jmxId获取dubbo sample
     * @param jmxId
     * @return
     */
    DubboDO getByJmxId(Long jmxId);

    /**
     * 心脏dubbo sample
     * @param dubboDO
     */
    void addDubbo(DubboDO dubboDO);
}
