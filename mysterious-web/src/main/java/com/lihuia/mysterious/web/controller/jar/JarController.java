package com.lihuia.mysterious.web.controller.jar;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jar.IJarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/3 10:13 PM
 */

@RestController
@RequestMapping(value = "/jar")
public class JarController {

    @Autowired
    private IJarService jarService;

    @PostMapping(value = "/upload")
    public Response<Boolean> uploadJar(@RequestParam(value = "testCaseId") Long testCaseId,
                                       @RequestParam(value = "jarFile") MultipartFile jarFile) {
        return ResponseUtil.buildSuccessResponse(jarService.uploadJar(testCaseId, jarFile));
    }

    @PostMapping(value = "/add")
    public Response<Long> addJar(@RequestBody JarVO jarVO) {
        return ResponseUtil.buildSuccessResponse(jarService.addJar(jarVO));
    }

    @PostMapping(value = "/update")
    public Response<Boolean> updateJar(@RequestBody JarVO jarVO) {
        return ResponseUtil.buildSuccessResponse(jarService.updateJar(jarVO));
    }

    @GetMapping(value = "/list")
    public Response<PageVO<JarVO>> getJarList(JarQuery jarQuery) {
        return ResponseUtil.buildSuccessResponse(jarService.getJarList(jarQuery));
    }

    @GetMapping(value = "/getByTestCaseId")
    public Response<List<JarVO>> getByTestCaseId(@RequestParam(value = "testCaseId") Long testCaseId) {
        return ResponseUtil.buildSuccessResponse(jarService.getByTestCaseId(testCaseId));
    }

    @GetMapping(value = "/delete")
    public Response<Boolean> deleteJar(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(jarService.deleteJar(id));
    }
}
