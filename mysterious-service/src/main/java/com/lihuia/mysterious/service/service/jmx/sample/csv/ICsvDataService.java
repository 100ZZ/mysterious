package com.lihuia.mysterious.service.service.jmx.sample.csv;

import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvDataVO;

/**
 * @author lihuia.com
 * @date 2024/10/11 14:48
 */
public interface ICsvDataService {

    /**
     * 根据jmxId获取CsvDataVO
     * @param jmxId
     * @return
     */
    CsvDataVO getByJmxId(Long jmxId);

    /**
     * 新增CsvDataVO
     * @param csvDataVO
     */
    void addCsvData(CsvDataVO csvDataVO);

    /**
     * 根据jmxId删除CsvDataVO
     * @param jmxId
     */
    void deleteByJmxId(Long jmxId);
}
