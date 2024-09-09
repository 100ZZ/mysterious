package com.lihuia.mysterious.service.service.jmx.thread;

import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface ISteppingThreadGroupService {

    /**
     * 根据jmxId获取梯度线程组内容
     * @param jmxId
     * @return
     */
    SteppingThreadGroupDO getByJmxId(Long jmxId);

    /**
     * 新增梯度线程组
     * @param steppingThreadGroupDO
     */
    void addSteppingThreadGroup(SteppingThreadGroupDO steppingThreadGroupDO);

    /**
     * 更新梯度线程组
     * @param steppingThreadGroupDO
     */
    void updateSteppingThreadGroup(SteppingThreadGroupDO steppingThreadGroupDO);

    /**
     * 删除
     * @param id
     */
    void deleteSteppingThreadGroup(Long id);
}
