package com.lihuia.mysterious.web.controller.jmx;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2022/4/4 2:25 PM
 */

@RestController
@RequestMapping(value = "/jmx")
public class JmxController {

    @Autowired
    private IJmxService jmxService;

    @PostMapping(value = "/upload")
    public Response<Boolean> uploadJmx(@RequestParam(value = "testCaseId") Long testCaseId,
                                       @RequestParam(value = "jmxFile") MultipartFile jmxFile) {
        return ResponseUtil.buildSuccessResponse(jmxService.uploadJmx(testCaseId, jmxFile));
    }

    @PostMapping(value = "/update")
    public Response<Boolean> updateJmx(@RequestBody JmxVO jmxVO) {
        return ResponseUtil.buildSuccessResponse(jmxService.updateJmx(jmxVO));
    }

    @GetMapping(value = "/delete")
    public Response<Boolean> deleteJxm(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.deleteJmx(id));
    }

    @GetMapping(value = "/list")
    public Response<PageVO<JmxVO>> getJmxList(JmxQuery jmxQuery) {
        return ResponseUtil.buildSuccessResponse(jmxService.getJmxList(jmxQuery));
    }

    @PostMapping(value = "/addOnline")
    public Response<Boolean> addOnlineJmx(@RequestBody JmxVO jmxVO) {
        return ResponseUtil.buildSuccessResponse(jmxService.addOnlineJmx(jmxVO));
    }

    @GetMapping(value = "/getOnline")
    public Response<JmxVO> getOnlineJmx(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.getOnlineJmx(id));
    }

    @PostMapping(value = "/updateOnline")
    public Response<Boolean> updateOnlineJmx(@RequestBody JmxVO jmxVO) {
        return ResponseUtil.buildSuccessResponse(jmxService.updateOnlineJmx(jmxVO));
    }

    @GetMapping(value = "/forceDelete")
    public Response<Boolean> forceDeleteJmx(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(jmxService.forceDelete(id));
    }
}
