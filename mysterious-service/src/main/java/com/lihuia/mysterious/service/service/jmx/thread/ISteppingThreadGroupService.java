package com.lihuia.mysterious.service.service.jmx.thread;

import com.lihuia.mysterious.core.vo.jmx.thread.SteppingThreadGroupVO;

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
    SteppingThreadGroupVO getByJmxId(Long jmxId);

    /**
     * 新增梯度线程组
     * @param steppingThreadGroupVO
     */
    void addSteppingThreadGroup(SteppingThreadGroupVO steppingThreadGroupVO);

    /**
     * 更新梯度线程组
     * @param steppingThreadGroupVO
     */
    void updateSteppingThreadGroup(SteppingThreadGroupVO steppingThreadGroupVO);

    /**
     * 删除
     * @param id
     */
    void deleteSteppingThreadGroup(Long id);
}
