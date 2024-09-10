package com.lihuia.mysterious.service.service.jmx.thread;

import com.lihuia.mysterious.core.vo.jmx.thread.ConcurrencyThreadGroupVO;

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
    ConcurrencyThreadGroupVO getByJmxId(Long jmxId);

    /**
     * 新增梯度线程组
     * @param concurrencyThreadGroupVO
     */
    void addConcurrencyThreadGroup(ConcurrencyThreadGroupVO concurrencyThreadGroupVO);

    /**
     * 删除
     * @param id
     */
    void deleteConcurrencyThreadGroup(Long id);

    /**
     * 更新
     * @param concurrencyThreadGroupVO
     */
    void updateConcurrencyThreadGroup(ConcurrencyThreadGroupVO concurrencyThreadGroupVO);
}
