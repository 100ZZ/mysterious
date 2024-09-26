package com.lihuia.mysterious.web.controller.jar;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jar.IJarService;
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
 * @date 2023/4/3 10:13 PM
 */

@RestController
@Api(tags = "JAR依赖管理")
@RequestMapping(value = "/jar")
public class JarController {

    @Autowired
    private IJarService jarService;

    @ApiOperation("上传")
    @PostMapping(value = "/upload/{testCaseId}")
    public Response<Boolean> uploadJar(@PathVariable Long testCaseId,
                                       @RequestParam(value = "jarFile") MultipartFile jarFile) {
        return ResponseUtil.buildSuccessResponse(jarService.uploadJar(testCaseId, jarFile, UserUtils.getCurrent()));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<JarVO>> getJarList(JarQuery jarQuery) {
        return ResponseUtil.buildSuccessResponse(jarService.getJarList(jarQuery));
    }

    @ApiOperation("查询用例关联的JAR依赖")
    @GetMapping(value = "/getByTestCaseId")
    public Response<List<JarVO>> getByTestCaseId(@RequestParam(value = "testCaseId") Long testCaseId) {
        return ResponseUtil.buildSuccessResponse(jarService.getByTestCaseId(testCaseId));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteJar(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(jarService.deleteJar(id));
    }

    @ApiOperation("依赖下载")
    @GetMapping(value = "/download/{id}")
    public void downloadJmx(@PathVariable Long id, HttpServletResponse response) throws IOException {
        JarVO jarVO = jarService.getJarVO(id);
        String fileName = jarVO.getSrcName();
        if (StringUtils.isBlank(fileName)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        String filePath = jarVO.getJarDir() + fileName;
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
