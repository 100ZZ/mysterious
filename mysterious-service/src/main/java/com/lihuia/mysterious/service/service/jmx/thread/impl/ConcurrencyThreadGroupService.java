package com.lihuia.mysterious.service.service.jmx.thread.impl;

import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.mapper.jmx.thread.ConcurrencyThreadGroupMapper;
import com.lihuia.mysterious.service.service.jmx.thread.IConcurrencyThreadGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class ConcurrencyThreadGroupService implements IConcurrencyThreadGroupService {

    @Autowired
    private ConcurrencyThreadGroupMapper concurrencyThreadGroupMapper;

    @Override
    public ConcurrencyThreadGroupDO getByJmxId(Long jmxId) {
        return concurrencyThreadGroupMapper.getByJmxId(jmxId);
    }

    @Override
    public void addConcurrencyThreadGroup(ConcurrencyThreadGroupDO concurrencyThreadGroupDO) {
        concurrencyThreadGroupMapper.add(concurrencyThreadGroupDO);
    }

    @Override
    public void deleteConcurrencyThreadGroup(Long id) {
        concurrencyThreadGroupMapper.delete(id);
    }

    @Override
    public void updateConcurrencyThreadGroup(ConcurrencyThreadGroupDO concurrencyThreadGroupDO) {
        concurrencyThreadGroupMapper.update(concurrencyThreadGroupDO);
    }
}
