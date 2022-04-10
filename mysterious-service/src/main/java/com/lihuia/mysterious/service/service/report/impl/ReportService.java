package com.lihuia.mysterious.service.service.report.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportByTestCaseQuery;
import com.lihuia.mysterious.core.vo.report.ReportQuery;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.service.report.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2022/4/5 5:45 PM
 */

@Slf4j
@Service
public class ReportService implements IReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private CRUDEntity<ReportDO> crudEntity;

    @Autowired
    private MysteriousFileUtils fileUtils;


    @Override
    public PageVO<ReportVO> getReportList(ReportQuery query) {
        String name = query.getName();
        Long creatorId = query.getCreatorId();
        Integer page = query.getPage();
        Integer size = query.getSize();
        PageVO<ReportVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(page, size);
        Integer total = reportMapper.getReportCount(name, creatorId);
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<ReportDO> reportList = reportMapper.getReportList(name, creatorId, offset, size);
            if (!CollectionUtils.isEmpty(reportList)) {
                pageVO.setList(reportList.stream().map(reportDO -> {
                    ReportVO reportVO = BeanConverter.doSingle(reportDO, ReportVO.class);
                    reportVO.setId(reportDO.getId());
                    return reportVO;
                }).collect(Collectors.toList()));
            }
        }
        return pageVO;
    }

    @Override
    public PageVO<ReportVO> getReportListByTestCase(ReportByTestCaseQuery query) {
        String name = query.getName();
        Long testCaseId = query.getTestCaseId();
        Long creatorId = query.getCreatorId();
        Integer page = query.getPage();
        Integer size = query.getSize();
        PageVO<ReportVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(page, size);
        Integer total = reportMapper.getReportCountByTestCase(name,testCaseId, creatorId);
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<ReportDO> reportList = reportMapper.getReportListByTestCase(name, creatorId, testCaseId, offset, size);
            if (!CollectionUtils.isEmpty(reportList)) {
                pageVO.setList(reportList.stream().map(reportDO -> {
                    ReportVO reportVO = BeanConverter.doSingle(reportDO, ReportVO.class);
                    reportVO.setId(reportDO.getId());
                    return reportVO;
                }).collect(Collectors.toList()));
            }
        }
        return pageVO;
    }

    @Override
    public List<ReportVO> getDebugReportListByTestCaseId(Long testCaseId, Integer execType, Integer limit) {
        return null;
    }

    @Override
    public ReportVO getById(Long id) {
        return null;
    }

    @Override
    public Long addReport(ReportVO reportVO) {
        return null;
    }

    @Override
    public Boolean deleteReport(Long id) {
        return null;
    }

    @Override
    public Boolean cleanReport(Long id) {
        return null;
    }

    @Override
    public Boolean updateReport(ReportVO reportVO) {
        return null;
    }

    @Override
    public FileSystemResource downloadReport(ReportVO reportVO) throws IOException {
        return null;
    }

    @Override
    public String getMasterJmeterLogLink(Long id) {
        return null;
    }

    @Override
    public String viewReport(Long id) {
        return null;
    }

    @Override
    public Boolean updateReportExecType() {
        return null;
    }
}
