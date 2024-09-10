package com.lihuia.mysterious.core.mapper.jmx.sample.http;


import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpHeaderDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */
public interface HttpHeaderMapper extends BaseMapper<HttpHeaderDO> {

    List<HttpHeaderDO> getListByHttpId(Long httpId);

    List<HttpHeaderDO> getListByJmxId(Long jmxId);

    List<HttpHeaderDO> getExistHeaderList(Long httpId, String headerKey);
}
