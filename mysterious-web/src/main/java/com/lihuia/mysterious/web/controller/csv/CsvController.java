package com.lihuia.mysterious.web.controller.csv;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.csv.CsvQuery;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.csv.ICsvService;
import com.lihuia.mysterious.web.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/10 4:05 PM
 */

@RestController
@Api(tags = "CSV文件管理")
@RequestMapping(value = "/csv")
public class CsvController {

    @Autowired
    private ICsvService csvService;

    @ApiOperation("上传")
    @PostMapping(value = "/upload/{testCaseId}")
    public Response<Boolean> uploadCsv(@PathVariable Long testCaseId,
                                       @RequestParam(value = "csvFile") MultipartFile csvFile) {
        return ResponseUtil.buildSuccessResponse(csvService.uploadCsv(testCaseId, csvFile, UserUtils.getCurrent()));
    }

    @ApiOperation("分页列表")
    @GetMapping(value = "/list")
    public Response<PageVO<CsvVO>> getCsvList(CsvQuery csvQuery) {
        return ResponseUtil.buildSuccessResponse(csvService.getCsvList(csvQuery));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteTestCase(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(csvService.deleteCsv(id));
    }

    @ApiOperation("查询用例关联的CSV文件")
    @GetMapping(value = "/getByTestCaseId")
    public Response<List<CsvVO>> getByTestCaseId(@RequestParam(value = "testCaseId") Long testCaseId) {
        return ResponseUtil.buildSuccessResponse(csvService.getByTestCaseId(testCaseId));
    }

    @ApiOperation("文件预览")
    @GetMapping(value = "/view/{id}")
    public void viewCsv(@PathVariable Long id,
                            HttpServletResponse response) {
        CsvVO csvVO = csvService.getCsvVO(id);
        String fileName = csvVO.getSrcName();
        String filePath = csvVO.getCsvDir() + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        try {
            InputStream inputStream = new FileInputStream(filePath);
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 循环取出流中的数据
            response.getOutputStream().write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.DOWNLOAD_ERROR);
        }
//        return ResponseUtil.buildSuccessResponse(ResponseCodeEnum.SUCCESS.getSuccess());
    }

    @ApiOperation("文件下载")
    @GetMapping(value = "/download/{id}")
    public void downloadJmx(@PathVariable Long id, HttpServletResponse response) throws IOException {
        CsvVO csvVO = csvService.getCsvVO(id);
        String fileName = csvVO.getSrcName();
        if (StringUtils.isBlank(fileName)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        String filePath = csvVO.getCsvDir() + fileName;
        InputStream inputStream = new FileInputStream(filePath);// 文件的存放路径
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(filePath).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;

        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }
}
