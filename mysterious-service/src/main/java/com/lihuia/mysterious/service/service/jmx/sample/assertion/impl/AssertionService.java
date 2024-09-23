package com.lihuia.mysterious.service.service.jmx.sample.assertion.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.assertion.AssertionDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.assertion.AssertionMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.assertion.AssertionVO;
import com.lihuia.mysterious.service.service.jmx.sample.assertion.IAssertionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/23 10:53
 */

@Service
public class AssertionService implements IAssertionService {

    @Autowired
    private AssertionMapper assertionMapper;

    @Override
    public Long addAssertion(AssertionVO assertionVO) {
        AssertionDO assertionDO = BeanConverter.doSingle(assertionVO, AssertionDO.class);
        assertionMapper.add(assertionDO);
        return assertionDO.getId();
    }

    @Override
    public void updateAssertion(AssertionVO assertionVO) {
        assertionMapper.update(BeanConverter.doSingle(assertionVO, AssertionDO.class));
    }

    @Override
    public void deleteAssertion(Long id) {
        assertionMapper.delete(id);
    }

    @Override
    public AssertionVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(assertionMapper.getByJmxId(jmxId), AssertionVO.class);
    }
}
