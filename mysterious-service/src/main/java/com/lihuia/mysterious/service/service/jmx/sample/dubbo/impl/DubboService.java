package com.lihuia.mysterious.service.service.jmx.sample.dubbo.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.dubbo.DubboMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboVO;
import com.lihuia.mysterious.service.service.jmx.sample.dubbo.IDubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class DubboService implements IDubboService {

    @Autowired
    private DubboMapper dubboMapper;
    @Override
    public DubboVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(dubboMapper.getByJmxId(jmxId), DubboVO.class);
    }

    @Override
    public void addDubbo(DubboVO dubboVO) {
        dubboMapper.add(BeanConverter.doSingle(dubboVO, DubboDO.class));
    }

    @Override
    public void updateDubbo(DubboVO dubboVO) {
        dubboMapper.update(BeanConverter.doSingle(dubboVO, DubboDO.class));
    }

    @Override
    public void deleteDubbo(Long id) {
        dubboMapper.delete(id);
    }
}
