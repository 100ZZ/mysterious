package com.lihuia.mysterious.core.mapper.jmx.sample.dubbo;

import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboMethodArgsDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:03
 */
public interface DubboMethodArgsMapper extends BaseMapper<DubboMethodArgsDO> {

    List<DubboMethodArgsDO> getListByDubboId(Long dubboId);

    List<DubboMethodArgsDO> getListByJmxId(Long jmxId);
    List<DubboMethodArgsDO> getExistParamList(Long dubboId, String paramType);
}
