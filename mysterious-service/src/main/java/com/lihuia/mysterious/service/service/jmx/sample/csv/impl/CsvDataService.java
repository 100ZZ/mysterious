package com.lihuia.mysterious.service.service.jmx.sample.csv.impl;

import com.lihuia.mysterious.core.entity.jmx.sample.csv.CsvDataDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.csv.CsvDataMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvDataVO;
import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvFileVO;
import com.lihuia.mysterious.service.service.jmx.sample.csv.ICsvDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/11 14:49
 */

@Slf4j
@Service
public class CsvDataService implements ICsvDataService {

    @Autowired
    private CsvDataMapper csvDataMapper;

    @Override
    public CsvDataVO getByJmxId(Long jmxId) {
        List<CsvDataDO> csvDataDOList = csvDataMapper.getByJmxId(jmxId);
        if (CollectionUtils.isEmpty(csvDataDOList)) {
            return null;
        }
        CsvDataDO firstCsvDataDO = csvDataDOList.get(0);
        CsvDataVO csvDataVO = new CsvDataVO();
        csvDataVO.setTestCaseId(firstCsvDataDO.getTestCaseId());
        csvDataVO.setJmxId(firstCsvDataDO.getJmxId());
        csvDataVO.setIgnoreFirstLine(firstCsvDataDO.getIgnoreFirstLine());
        csvDataVO.setRecycleOnEof(firstCsvDataDO.getRecycleOnEof());
        csvDataVO.setStopThreadOnEof(firstCsvDataDO.getStopThreadOnEof());
        csvDataVO.setAllowQuotedData(firstCsvDataDO.getAllowQuotedData());
        csvDataVO.setFileEncoding(firstCsvDataDO.getFileEncoding());
        csvDataVO.setSharingMode(firstCsvDataDO.getSharingMode());

        List<CsvFileVO> csvFileVOList = new ArrayList<>();
        for (CsvDataDO csvDataDO : csvDataDOList) {
            CsvFileVO csvFileVO = new CsvFileVO();
            csvFileVO.setFilename(csvDataDO.getFilename());
            csvFileVO.setVariableNames(csvDataDO.getVariableNames());
            csvFileVO.setDelimiter(csvDataDO.getDelimiter());
            csvFileVOList.add(csvFileVO);
        }
        csvDataVO.setCsvFileVOList(csvFileVOList);
        return csvDataVO;
    }

    @Override
    public void addCsvData(CsvDataVO csvDataVO) {
        List<CsvFileVO> csvFileVOList = csvDataVO.getCsvFileVOList();

        Long testCaseId = csvDataVO.getTestCaseId();
        Long jmxId = csvDataVO.getJmxId();
        String fileEncoding = csvDataVO.getFileEncoding();
        Integer ignoreFirstLine = csvDataVO.getIgnoreFirstLine();
        Integer allowQuotedData = csvDataVO.getAllowQuotedData();
        Integer recycleOnEof = csvDataVO.getRecycleOnEof();
        Integer stopThreadOnEof = csvDataVO.getStopThreadOnEof();
        String sharingMode = csvDataVO.getSharingMode();
        for (CsvFileVO csvFileVO : csvFileVOList) {
            CsvDataDO csvDataDO = new CsvDataDO();
            csvDataDO.setTestCaseId(testCaseId);
            csvDataDO.setJmxId(jmxId);
            csvDataDO.setFilename(csvFileVO.getFilename());
            csvDataDO.setVariableNames(csvFileVO.getVariableNames());
            csvDataDO.setDelimiter(csvFileVO.getDelimiter());
            csvDataDO.setFileEncoding(fileEncoding);
            csvDataDO.setIgnoreFirstLine(ignoreFirstLine);
            csvDataDO.setAllowQuotedData(allowQuotedData);
            csvDataDO.setRecycleOnEof(recycleOnEof);
            csvDataDO.setStopThreadOnEof(stopThreadOnEof);
            csvDataDO.setSharingMode(sharingMode);
            csvDataMapper.add(csvDataDO);
            log.info("添加CSV文件：{}", csvDataDO);
        }
    }

    @Override
    public void deleteByJmxId(Long jmxId) {
        csvDataMapper.deleteByJmxId(jmxId);
    }
}
