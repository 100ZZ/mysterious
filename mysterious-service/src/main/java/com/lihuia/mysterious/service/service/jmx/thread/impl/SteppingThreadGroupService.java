package com.lihuia.mysterious.service.service.jmx.thread.impl;

import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.mapper.jmx.thread.SteppingThreadGroupMapper;
import com.lihuia.mysterious.service.service.jmx.thread.ISteppingThreadGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class SteppingThreadGroupService implements ISteppingThreadGroupService {

    @Autowired
    private SteppingThreadGroupMapper steppingThreadGroupMapper;

    @Override
    public SteppingThreadGroupDO getByJmxId(Long jmxId) {
        return steppingThreadGroupMapper.getByJmxId(jmxId);
    }

    @Override
    public void addSteppingThreadGroup(SteppingThreadGroupDO steppingThreadGroupDO) {
        steppingThreadGroupMapper.add(steppingThreadGroupDO);
    }

    @Override
    public void updateSteppingThreadGroup(SteppingThreadGroupDO steppingThreadGroupDO) {
        steppingThreadGroupMapper.update(steppingThreadGroupDO);
    }

    @Override
    public void deleteSteppingThreadGroup(Long id) {
        steppingThreadGroupMapper.delete(id);
    }
}
