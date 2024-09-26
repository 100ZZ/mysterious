package com.lihuia.mysterious.web.controller.jmx;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
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

/**
 * @author lihuia.com
 * @date 2023/4/4 2:25 PM
 */

@RestController
@Api(tags = "JMX脚本管理")
@RequestMapping(value = "/jmx")
public class JmxController {

    @Autowired
    private IJmxService jmxService;

    @ApiOperation("上传")
    @PostMapping(value = "/upload/{testCaseId}")
    public Response<Boolean> uploadJmx(@PathVariable Long testCaseId,
                                       @RequestParam(value = "jmxFile") MultipartFile jmxFile) {
        return ResponseUtil.buildSuccessResponse(jmxService.uploadJmx(testCaseId, jmxFile, UserUtils.getCurrent()));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteJxm(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.deleteJmx(id));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<JmxVO>> getJmxList(JmxQuery jmxQuery) {
        return ResponseUtil.buildSuccessResponse(jmxService.getJmxList(jmxQuery));
    }

    @ApiOperation("新增在线脚本")
    @PostMapping(value = "/addOnline")
    public Response<Boolean> addOnlineJmx(@RequestBody JmxVO jmxVO) {
        return ResponseUtil.buildSuccessResponse(jmxService.addOnlineJmx(jmxVO, UserUtils.getCurrent()));
    }

    @ApiOperation("查询在线脚本")
    @GetMapping(value = "/getOnline/{id}")
    public Response<JmxVO> getOnlineJmx(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.getOnlineJmx(id));
    }

    @ApiOperation("编辑在线脚本")
    @PostMapping(value = "/updateOnline/{id}")
    public Response<Boolean> updateOnlineJmx(@PathVariable Long id,
                                             @RequestBody JmxVO jmxVO) {
        return ResponseUtil.buildSuccessResponse(jmxService.updateOnlineJmx(id, jmxVO, UserUtils.getCurrent()));
    }

    @GetMapping(value = "/forceDelete/{id}")
    public Response<Boolean> forceDeleteJmx(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.forceDelete(id));
    }

    @ApiOperation("脚本预览")
    @GetMapping(value = "/view/{id}")
    public void viewJmx(@PathVariable Long id,
                            HttpServletResponse response) {
        JmxVO jmxVO = jmxService.getJmxVO(id);
        String fileName = jmxVO.getSrcName();
        String filePath = jmxVO.getJmxDir() + fileName;
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
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.DOWNLOAD_ERROR);
        }
    }

    @ApiOperation("脚本下载")
    @GetMapping(value = "/download/{id}")
    public void downloadJmx(@PathVariable Long id, HttpServletResponse response) throws IOException {
        JmxVO jmxVO = jmxService.getJmxVO(id);
        String fileName = jmxVO.getSrcName();
        if (StringUtils.isBlank(fileName)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        String filePath = jmxVO.getJmxDir() + fileName;
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
