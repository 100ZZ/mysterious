package com.lihuia.mysterious.core.mapper.jmx.thread;

import com.lihuia.mysterious.core.entity.jmx.thread.ThreadGroupDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;


/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface ThreadGroupMapper extends BaseMapper<ThreadGroupDO> {

    ThreadGroupDO getByJmxId(Long jmxId);
}
