package com.lihuia.mysterious.core.mapper.jmx.thread;


import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface ConcurrencyThreadGroupMapper extends BaseMapper<ConcurrencyThreadGroupDO> {

    ConcurrencyThreadGroupDO getByJmxId(Long jmxId);
}
