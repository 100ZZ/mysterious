package com.lihuia.mysterious.web.controller.report;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportByTestCaseQuery;
import com.lihuia.mysterious.core.vo.report.ReportQuery;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.service.enums.ReportTypeEnum;
import com.lihuia.mysterious.service.service.report.IReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/10 9:29 PM
 */

@Slf4j
@RestController
@Api(tags = "报告管理")
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @ApiOperation("报告列表")
    @GetMapping(value = "/list")
    public Response<PageVO<ReportVO>> getAllReportList(ReportQuery query) {
        return ResponseUtil.buildSuccessResponse(reportService.getReportList(query));
    }

    @ApiOperation("用例执行的报告列表")
    @GetMapping(value = "/listByTestCase")
    public Response<PageVO<ReportVO>> getReportList(ReportByTestCaseQuery query) {
        return ResponseUtil.buildSuccessResponse(reportService.getReportListByTestCase(query));
    }

    @ApiOperation("报告下载")
    @GetMapping(value = "/download/{id}")
    public Object downloadReport(@PathVariable Long id) throws IOException {
        ReportVO reportVO = reportService.getById(id);
        FileSystemResource reportZipFile = reportService.downloadReport(reportVO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        String fileNameUTF8 = new String(reportVO.getName().getBytes(), StandardCharsets.UTF_8);
        headers.add("Content-Disposition",
                "attachment;filename=" + fileNameUTF8 + ".zip");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(reportZipFile.contentLength())
                .body(new InputStreamResource(reportZipFile.getInputStream()));
    }

    @ApiOperation("查看日志")
    @GetMapping(value = "/getJMeterLog/{id}")
    public void getLog(@PathVariable Long id, HttpServletResponse response) {
        ReportVO reportVO = reportService.getById(id);
        String jmeterLogFilePath = reportVO.getJmeterLogFilePath();
        File file = new File(jmeterLogFilePath);

        if (!file.exists()) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        if (file.length() > 1024 * 1024) {
            throw new MysteriousException(ResponseCodeEnum.STRESS_LOG_TOO_LARGE);
        }

        try {
            InputStream inputStream = new FileInputStream(jmeterLogFilePath);
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + jmeterLogFilePath.substring(jmeterLogFilePath.lastIndexOf("/") + 1) + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.DOWNLOAD_ERROR);
        }
    }

    @ApiOperation("报告清理")
    @GetMapping(value = "/clean/{id}")
    public Response<Boolean> cleanReport(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(reportService.cleanReport(id));
    }

    @ApiOperation("报告预览")
    @GetMapping(value = "/view/{id}")
    public Response<String> viewReport(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(reportService.viewReport(id));
    }
}
