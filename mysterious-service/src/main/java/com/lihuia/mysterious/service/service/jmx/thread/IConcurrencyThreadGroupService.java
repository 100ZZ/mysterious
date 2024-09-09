package com.lihuia.mysterious.service.service.jmx.thread;

import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IConcurrencyThreadGroupService {

    /**
     * 根据jmxId获取梯度加压信息
     * @param jmxId
     * @return
     */
    ConcurrencyThreadGroupDO getByJmxId(Long jmxId);

    /**
     * 新增梯度线程组
     * @param concurrencyThreadGroupDO
     */
    void addConcurrencyThreadGroup(ConcurrencyThreadGroupDO concurrencyThreadGroupDO);

    /**
     * 删除
     * @param id
     */
    void deleteConcurrencyThreadGroup(Long id);

    /**
     * 更新
     * @param concurrencyThreadGroupDO
     */
    void updateConcurrencyThreadGroup(ConcurrencyThreadGroupDO concurrencyThreadGroupDO);
}
