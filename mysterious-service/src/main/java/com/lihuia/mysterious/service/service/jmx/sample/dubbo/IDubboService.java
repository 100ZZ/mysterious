package com.lihuia.mysterious.service.service.jmx.sample.dubbo;


import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboVO;

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
    DubboVO getByJmxId(Long jmxId);

    /**
     * 心脏dubbo sample
     * @param dubboVO
     */
    void addDubbo(DubboVO dubboVO);

    /**
     * 更新http sample
     * @param dubboVO
     */
    void updateDubbo(DubboVO dubboVO);

    /**
     * 删除
     * @param id
     */
    void deleteDubbo(Long id);
}
