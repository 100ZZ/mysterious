package com.lihuia.mysterious.core.mapper.jmx.sample.http;


import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpParamDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface HttpParamMapper extends BaseMapper<HttpParamDO> {

    List<HttpParamDO> getListByHttpId(Long httpId);

    List<HttpParamDO> getListByJmxId(Long jmxId);

    List<HttpParamDO> getExistParamList(Long httpId, String paramKey);
}
