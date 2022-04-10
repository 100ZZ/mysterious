package com.lihuia.mysterious.web.controller.jar;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jar.IJarService;
import com.lihuia.mysterious.web.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/3 10:13 PM
 */

@RestController
@Api(tags = "JAR依赖管理")
@RequestMapping(value = "/jar")
public class JarController {

    @Autowired
    private IJarService jarService;

    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    public Response<Boolean> uploadJar(@RequestParam(value = "testCaseId") Long testCaseId,
                                       @RequestParam(value = "jarFile") MultipartFile jarFile) {
        return ResponseUtil.buildSuccessResponse(jarService.uploadJar(testCaseId, jarFile, UserUtils.getCurrent()));
    }

    @ApiOperation("更新")
    @PostMapping(value = "/update")
    public Response<Boolean> updateJar(@RequestBody JarVO jarVO) {
        return ResponseUtil.buildSuccessResponse(jarService.updateJar(jarVO, UserUtils.getCurrent()));
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
    @GetMapping(value = "/delete")
    public Response<Boolean> deleteJar(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(jarService.deleteJar(id));
    }
}
