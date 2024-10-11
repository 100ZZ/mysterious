package com.lihuia.mysterious.core.mapper.jmx.sample.csv;

import com.lihuia.mysterious.core.entity.jmx.sample.csv.CsvDataDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/10 16:44
 */
public interface CsvDataMapper extends BaseMapper<CsvDataDO> {

    List<CsvDataDO> getByJmxId(Long jmxId);

    void deleteByJmxId(Long jmxId);
}
