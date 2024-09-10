package com.lihuia.mysterious.service.service.jmx.thread.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.mapper.jmx.thread.SteppingThreadGroupMapper;
import com.lihuia.mysterious.core.vo.jmx.thread.SteppingThreadGroupVO;
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
    public SteppingThreadGroupVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(steppingThreadGroupMapper.getByJmxId(jmxId), SteppingThreadGroupVO.class);
    }

    @Override
    public void addSteppingThreadGroup(SteppingThreadGroupVO steppingThreadGroupVO) {
        steppingThreadGroupMapper.add(BeanConverter.doSingle(steppingThreadGroupVO, SteppingThreadGroupDO.class));
    }

    @Override
    public void updateSteppingThreadGroup(SteppingThreadGroupVO steppingThreadGroupVO) {
        steppingThreadGroupMapper.update(BeanConverter.doSingle(steppingThreadGroupVO, SteppingThreadGroupDO.class));
    }

    @Override
    public void deleteSteppingThreadGroup(Long id) {
        steppingThreadGroupMapper.delete(id);
    }
}
