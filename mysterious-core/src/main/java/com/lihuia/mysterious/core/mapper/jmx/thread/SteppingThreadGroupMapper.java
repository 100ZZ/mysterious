package com.lihuia.mysterious.core.mapper.jmx.thread;


import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface SteppingThreadGroupMapper extends BaseMapper<SteppingThreadGroupDO> {

    SteppingThreadGroupDO getByJmxId(Long jmxId);
}
