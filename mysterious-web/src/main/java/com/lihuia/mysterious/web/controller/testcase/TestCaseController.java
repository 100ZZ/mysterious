package com.lihuia.mysterious.web.controller.testcase;

import com.alibaba.fastjson.JSONObject;
import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseParam;
import com.lihuia.mysterious.core.vo.testcase.TestCaseQuery;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import com.lihuia.mysterious.web.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/10 4:24 PM
 */

@RestController
@Api(tags = "用例管理")
@RequestMapping(value = "/testcase")
public class TestCaseController {

    @Autowired
    private ITestCaseService testCaseService;

    @ApiOperation("新增用例")
    @PostMapping(value = "/add")
    public Response<Long> addTestCase(@RequestBody TestCaseParam testCaseParam) {
        return ResponseUtil.buildSuccessResponse(testCaseService.addTestCase(testCaseParam, UserUtils.getCurrent()));
    }

    @ApiOperation("批量删除")
    @PostMapping(value = "/batchDelete")
    public Response<Boolean> batchDeleteTestCase(@RequestBody JSONObject params) {
        List<Long> ids = params.getJSONArray("ids").toJavaList(Long.class);
        return ResponseUtil.buildSuccessResponse(testCaseService.batchDeleteTestCase(ids));
    }

    @ApiOperation("删除用例")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteTestCase(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(testCaseService.deleteTestCase(id));
    }

    @ApiOperation("调试用例")
    @GetMapping(value = "/debug/{id}")
    public Response<Boolean> debugTestCase(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(testCaseService.debugTestCase(id, UserUtils.getCurrent()));
    }

    @ApiOperation("执行用例")
    @GetMapping(value = "/run/{id}")
    public Response<Boolean> startTestCase(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(testCaseService.runTestCase(id, UserUtils.getCurrent()));
    }

    @ApiOperation("结束执行")
    @GetMapping(value = "/stop/{id}")
    public Response<Boolean> stopTestCase(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(testCaseService.stopTestCase(id, UserUtils.getCurrent()));
    }

    @ApiOperation("更新用例")
    @PostMapping(value = "/update/{id}")
    public Response<Boolean> updateTestCase(@PathVariable Long id,
                                            @RequestBody TestCaseParam testCaseParam) {
        return ResponseUtil.buildSuccessResponse(testCaseService.updateTestCase(id, testCaseParam, UserUtils.getCurrent()));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<TestCaseVO>> getTestCaseList(TestCaseQuery testCaseQuery) {
        return ResponseUtil.buildSuccessResponse(testCaseService.getTestCaseList(testCaseQuery));
    }

    @ApiOperation("查询所有依赖信息")
    @GetMapping(value = "/getFull/{id}")
    public Response<TestCaseFullVO> getFull(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(testCaseService.getFull(id));
    }

    @ApiOperation("同步新增压力机测试数据")
    @GetMapping(value = "/syncNode")
    public Response<Boolean> syncNode(@RequestParam(value = "nodeId") Long nodeId) {
        return ResponseUtil.buildSuccessResponse(testCaseService.syncNode(nodeId));
    }

}
