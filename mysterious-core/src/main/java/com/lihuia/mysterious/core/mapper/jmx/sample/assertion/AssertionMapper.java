package com.lihuia.mysterious.core.mapper.jmx.sample.assertion;

import com.lihuia.mysterious.core.entity.jmx.sample.assertion.AssertionDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

/**
 * @author lihuia.com
 * @date 2024/9/23 09:56
 */
public interface AssertionMapper extends BaseMapper<AssertionDO> {

    AssertionDO getByJmxId(Long jmxId);
}
