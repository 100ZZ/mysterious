package com.lihuia.mysterious.core.mapper.jmx;

import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午3:33
 */

public interface JmxMapper extends BaseMapper<JmxDO> {

    Integer getJmxCount(String srcName, Long testCaseId, Long creatorId);

    List<JmxDO> getJmxList(String srcName, Long testCaseId, Long creatorId, Integer offset, Integer limit);

    JmxDO getByTestCaseId(Long testCaseId);

    List<JmxDO> getExistJmxList(Long testCaseId, String srcName, String jmxDir);
}
