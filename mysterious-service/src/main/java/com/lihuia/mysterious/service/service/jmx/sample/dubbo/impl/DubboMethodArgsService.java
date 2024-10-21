package com.lihuia.mysterious.service.service.jmx.sample.dubbo.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboMethodArgsDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.dubbo.DubboMethodArgsMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboMethodArgsVO;
import com.lihuia.mysterious.service.service.jmx.sample.dubbo.IDubboMethodArgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:24
 */

@Service
public class DubboMethodArgsService implements IDubboMethodArgsService {

    @Autowired
    private DubboMethodArgsMapper dubboMethodArgsMapper;

    @Override
    public void addDubboMethodArgs(DubboMethodArgsVO dubboMethodArgsVO) {
        dubboMethodArgsMapper.add(BeanConverter.doSingle(dubboMethodArgsVO, DubboMethodArgsDO.class));
    }

    @Override
    public List<DubboMethodArgsVO> getListByDubboId(Long dubboId) {
        return BeanConverter.doList(dubboMethodArgsMapper.getListByDubboId(dubboId), DubboMethodArgsVO.class);
    }

    @Override
    public List<DubboMethodArgsVO> getListByJmxId(Long jmxId) {
        return BeanConverter.doList(dubboMethodArgsMapper.getListByJmxId(jmxId), DubboMethodArgsVO.class);
    }

    @Override
    public void deleteDubboMethodArgs(Long id) {
        dubboMethodArgsMapper.delete(id);
    }

    @Override
    public void batchDeleteDubboMethodArgs(List<Long> ids) {
        dubboMethodArgsMapper.batchDelete(ids);
    }

    @Override
    public List<DubboMethodArgsVO> getExistMethodArgsList(Long dubboId, String paramType) {
        return BeanConverter.doList(dubboMethodArgsMapper.getExistParamList(dubboId, paramType), DubboMethodArgsVO.class);
    }
}
