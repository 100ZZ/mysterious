package com.lihuia.mysterious.core.mapper.jmx;


import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface JmxMapper extends BaseMapper<JmxDO> {

    Integer getJmxCount(String srcName, Long testCaseId);

    List<JmxDO> getJmxList(String srcName, Long testCaseId, Integer offset, Integer limit);

    JmxDO getByTestCaseId(Long testCaseId);

    List<JmxDO> getExistJmxList(Long testCaseId, String srcName, String jmxDir);
}
