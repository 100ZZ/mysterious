package com.lihuia.mysterious.core.mapper.jmx.sample.csv;

import com.lihuia.mysterious.core.entity.jmx.sample.csv.CsvDataDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

/**
 * @author lihuia.com
 * @date 2024/10/10 16:44
 */
public interface CsvdataMapper extends BaseMapper<CsvDataDO> {

    CsvDataDO getByJmxId(Long jmxId);
}
