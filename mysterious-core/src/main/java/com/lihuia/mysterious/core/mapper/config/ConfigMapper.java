package com.lihuia.mysterious.core.mapper.config;

import com.lihuia.mysterious.core.entity.config.ConfigDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:36
 */

public interface ConfigMapper extends BaseMapper<ConfigDO> {

    Integer getConfigCount(String key);

    List<ConfigDO> getConfigList(String key, Integer offset, Integer limit);

    String getValue(String key);

    List<ConfigDO> getByKey(String key);
}
