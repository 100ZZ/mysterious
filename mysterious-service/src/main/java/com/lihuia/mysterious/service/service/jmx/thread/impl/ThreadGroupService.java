package com.lihuia.mysterious.service.service.jmx.thread.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class ThreadGroupService implements IThreadGroupService {

    @Autowired
    private ThreadGroupMapper threadGroupMapper;

    @Override
    public ThreadGroupDO getByJmxId(Long jmxId) {
        return threadGroupMapper.getByJmxId(jmxId);
    }

    @Override
    public void addThreadGroup(ThreadGroupDO threadGroupDO) {
        threadGroupMapper.add(threadGroupDO);
    }

    @Override
    public void updateThreadGroup(ThreadGroupDO threadGroupDO) {
        threadGroupMapper.update(threadGroupDO);
    }

    @Override
    public void deleteThreadGroup(Long id) {
        threadGroupMapper.delete(id);
    }
}
