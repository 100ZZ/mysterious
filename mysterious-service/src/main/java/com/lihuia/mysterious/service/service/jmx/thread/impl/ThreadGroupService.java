package com.lihuia.mysterious.service.service.jmx.thread.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.thread.ThreadGroupDO;
import com.lihuia.mysterious.core.mapper.jmx.thread.ThreadGroupMapper;
import com.lihuia.mysterious.core.vo.jmx.thread.ThreadGroupVO;
import com.lihuia.mysterious.service.service.jmx.thread.IThreadGroupService;
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
    public ThreadGroupVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(threadGroupMapper.getByJmxId(jmxId), ThreadGroupVO.class);
    }

    @Override
    public void addThreadGroup(ThreadGroupVO threadGroupVO) {
        threadGroupMapper.add(BeanConverter.doSingle(threadGroupVO, ThreadGroupDO.class));
    }

    @Override
    public void updateThreadGroup(ThreadGroupVO threadGroupVO) {
        threadGroupMapper.update(BeanConverter.doSingle(threadGroupVO, ThreadGroupDO.class));
    }

    @Override
    public void deleteThreadGroup(Long id) {
        threadGroupMapper.delete(id);
    }
}
