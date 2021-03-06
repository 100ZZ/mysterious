package com.lihuia.mysterious.service.service.report.impl;

import com.lihuia.mysterious.common.compress.CompressUtil;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportByTestCaseQuery;
import com.lihuia.mysterious.core.vo.report.ReportQuery;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.ReportTypeEnum;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.report.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private IConfigService configService;


    @Override
    public PageVO<ReportVO> getReportList(ReportQuery query) {
        PageVO<ReportVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = reportMapper.getReportCount(query.getName(), query.getCreatorId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<ReportDO> reportList =
                    reportMapper.getReportList(query.getName(), query.getCreatorId(), offset, query.getSize());
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
        PageVO<ReportVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = reportMapper.getReportCountByTestCase(query.getName(), query.getTestCaseId(), query.getCreatorId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<ReportDO> reportList =
                    reportMapper.getReportListByTestCase(query.getName(), query.getTestCaseId(), query.getCreatorId(), offset, query.getSize());
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
        List<ReportDO> reportDOList = reportMapper.getReportByType(testCaseId, execType, limit);
        if (CollectionUtils.isEmpty(reportDOList)) {
            return Collections.emptyList();
        }
        return reportDOList.stream().map(reportDO -> {
            ReportVO reportVO = BeanConverter.doSingle(reportDO, ReportVO.class);
            reportVO.setId(reportDO.getId());
            return reportVO;
        }).collect(Collectors.toList());
    }

    @Override
    public ReportVO getById(Long id) {
        ReportDO reportDO = reportMapper.getById(id);
        if (!ObjectUtils.isEmpty(reportDO)) {
            ReportVO reportVO = BeanConverter.doSingle(reportDO, ReportVO.class);
            reportVO.setId(id);
            return reportVO;
        }
        return null;
    }

    @Override
    public Long addReport(ReportVO reportVO, UserVO userVO) {
        if (ObjectUtils.isEmpty(reportVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        ReportDO reportDO = BeanConverter.doSingle(reportVO, ReportDO.class);
        crudEntity.addT(reportDO, userVO);
        reportMapper.add(reportDO);
        return reportDO.getId();
    }

    @Override
    public Boolean deleteReport(Long id) {
        return reportMapper.delete(id) > 0;
    }

    @Transactional
    @Override
    public Boolean cleanReport(Long id) {
        /** ?????????????????????????????????????????????????????? */
        ReportDO reportDO = reportMapper.getById(id);
        if (ObjectUtils.isEmpty(reportDO)) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_NOT_EXIST);
        }
        log.info("??????????????????, id: {}", id);
        reportMapper.delete(id);
        /** ?????????????????????../?????????/data????????????????????????../?????????/jtl */
        String reportDir = reportDO.getReportDir();
        int lastIndex = reportDir.replaceAll("/$", "").lastIndexOf("/");
        String cleanDir = reportDir.substring(0, lastIndex);
        fileUtils.rmDir(cleanDir);
        return true;
    }

    public static void main(String[] args) {
        String ss = "/data/2022-04-04-10:35:00/log/";

        int lastIndex = ss.replaceAll("/$", "").lastIndexOf("/");
        System.out.println(lastIndex);
        System.out.println(ss.substring(0, lastIndex));
    }

    @Override
    public FileSystemResource downloadReport(ReportVO reportVO) throws IOException {
        /** ????????????????????????????????????????????????????????????????????????????????? */
        if (reportVO.getExecType().equals(ReportTypeEnum.DEBUG.getCode())) {
            throw new MysteriousException(ResponseCodeEnum.DEBUG_REPORT_NOT_DOWNLOAD);
        }
        /** ?????????????????????????????????????????????????????? */
//        if (testCaseService.getById(reportDO.getTestCaseId()).getStatus().equals(1)) {
//        }
        /** ?????????????????? */
        String reportDir = reportVO.getReportDir();

        File reportFilePath = new File(reportDir);
        if (!reportFilePath.exists()) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_DIR_NOT_EXIST);
        }
        /** ????????????data???????????? */
        if (reportFilePath.list().length == 0) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_DIR_IS_EMPTY);
        }

        /** ?????? /data/xxx/xxx/xxx/data/??????????????????????????????fix */
//        String reportPath = reportFilePath.replace("/data/", "/");
//        String srcReportPath = reportFilePath.replace("/data/", "/data");
        String reportPath = reportDir.substring(0, reportDir.lastIndexOf("data"));
        String srcReportPath = reportPath + "data";
        String reportZipPath = reportPath + reportVO.getName() + ".zip";
        log.info("reportPath: {}, srcReportPath: {}, reportZipPath: {}", reportPath, srcReportPath, reportZipPath);
        FileSystemResource zipFileResource = new FileSystemResource(reportZipPath);

        /** ??????????????????????????????????????????????????????????????????????????????????????? */
//        if (zipFileResource.exists()) {
//            log.info("??????zip: {}", reportZipPath);
//            fileUtils.rmFile(reportZipPath);
//        }
        /** ????????????????????? */
        if (!zipFileResource.exists()) {
            CompressUtil.compress(srcReportPath, reportZipPath);
        }
        return zipFileResource;
    }

    @Override
    public String viewReport(Long id) {
        ///??????csv??????_2022-03-25-17:09:34/report/2022-04-01-16:10:16/data/
        ReportDO reportDO = reportMapper.getById(id);
        if (ObjectUtils.isEmpty(reportDO)) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_NOT_EXIST);
        }
        if (!reportDO.getExecType().equals(ReportTypeEnum.RUNNING.getCode())) {
            throw new MysteriousException(ResponseCodeEnum.DEBUG_REPORT_NOT_VIEW);
        }
        /** ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         if (testCaseService.getById(reportDO.getTestCaseId()).getStatus().equals(1)) {
         }
         */
        String testCaseReportDir = reportDO.getReportDir();
        File reportDataPathFile = new File(testCaseReportDir);
        if (!reportDataPathFile.exists()) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_DIR_NOT_EXIST);
        }
        /** ????????????data???????????? */
        if (Objects.requireNonNull(reportDataPathFile.list()).length == 0) {
            throw new MysteriousException(ResponseCodeEnum.REPORT_DIR_IS_EMPTY);
        }
        String reportDir;
        if (testCaseReportDir.contains("mysterious-data")) {
            reportDir = testCaseReportDir.split("mysterious-data")[1];
        } else {
            reportDir = testCaseReportDir.replaceAll("^\\/data", "");
        }

        String host = configService.getValue("host");
        String port = configService.getValue("port");
        String url = "http://" + host + ":" + port + reportDir + "index.html";
        return url;
    }

    @Override
    public Boolean updateReportExecType() {
        return true;
    }
}
