package com.lihuia.mysterious.service.service.jmx.thread;

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
    ThreadGroupDO getByJmxId(Long jmxId);

    /**
     * 新增默认线程组
     * @param threadGroupDO
     */
    void addThreadGroup(ThreadGroupDO threadGroupDO);

    /**
     * 更新默认线程组
     * @param threadGroupDO
     */
    void updateThreadGroup(ThreadGroupDO threadGroupDO);

    /**
     * 删除
     * @param id
     */
    void deleteThreadGroup(Long id);
}
