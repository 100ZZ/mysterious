package com.lihuia.mysterious.core.mapper.jmx.sample.java;


import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface JavaMapper extends BaseMapper<JavaDO> {

    List<JavaDO> getByJmxId(Long jmxId);

    void deleteByJmxId(Long jmxId);
}
