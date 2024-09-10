package com.lihuia.mysterious.core.mapper.jmx.sample.http;

import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface HttpMapper extends BaseMapper<HttpDO> {

    HttpDO getByJmxId(Long jmxId);
}
