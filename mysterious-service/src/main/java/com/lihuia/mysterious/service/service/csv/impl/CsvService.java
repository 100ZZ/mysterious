package com.lihuia.mysterious.service.service.csv.impl;

import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.csv.ICsvService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:34
 */

@Service
public class CsvService implements ICsvService {
    @Override
    public void uploadCsv(Long testCaseId, MultipartFile csvFile) {

    }

    @Override
    public void addCsv(CsvVO csvVO) {

    }

    @Override
    public void deleteCsv(Long id) {

    }

    @Override
    public void updateCsv(CsvVO csvVO) {

    }

    @Override
    public PageVO<CsvVO> getCsvList(String srcName, Long testCaseId, Long creatorId, Integer page, Integer size) {
        return null;
    }

    @Override
    public List<CsvVO> getByTestCaseId(Long testCaseId) {
        return null;
    }

    @Override
    public CsvVO getDetail(Long id) {
        return null;
    }
}
