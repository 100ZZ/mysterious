package com.lihuia.mysterious.service.service.jmx.thread.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.mapper.jmx.thread.ConcurrencyThreadGroupMapper;
import com.lihuia.mysterious.core.vo.jmx.thread.ConcurrencyThreadGroupVO;
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
    public ConcurrencyThreadGroupVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(concurrencyThreadGroupMapper.getByJmxId(jmxId), ConcurrencyThreadGroupVO.class);
    }

    @Override
    public void addConcurrencyThreadGroup(ConcurrencyThreadGroupVO concurrencyThreadGroupVO) {
        concurrencyThreadGroupMapper.add(BeanConverter.doSingle(concurrencyThreadGroupVO, ConcurrencyThreadGroupDO.class));
    }

    @Override
    public void deleteConcurrencyThreadGroup(Long id) {
        concurrencyThreadGroupMapper.delete(id);
    }

    @Override
    public void updateConcurrencyThreadGroup(ConcurrencyThreadGroupVO concurrencyThreadGroupVO) {
        concurrencyThreadGroupMapper.update(BeanConverter.doSingle(concurrencyThreadGroupVO, ConcurrencyThreadGroupDO.class));
    }
}
