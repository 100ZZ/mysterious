package com.lihuia.mysterious.service.service.jmx.thread;

import com.lihuia.mysterious.core.vo.jmx.thread.ThreadGroupVO;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */


public interface IThreadGroupService {

    /**
     * 根据jmxId获取线程组内容
     * @param jmxId
     * @return
     */
    ThreadGroupVO getByJmxId(Long jmxId);

    /**
     * 新增默认线程组
     * @param threadGroupVO
     */
    void addThreadGroup(ThreadGroupVO threadGroupVO);

    /**
     * 更新默认线程组
     * @param threadGroupVO
     */
    void updateThreadGroup(ThreadGroupVO threadGroupVO);

    /**
     * 删除
     * @param id
     */
    void deleteThreadGroup(Long id);
}
