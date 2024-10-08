package com.lihuia.mysterious.web.controller.node;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.node.NodeParam;
import com.lihuia.mysterious.core.vo.node.NodeQuery;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.node.INodeService;
import com.lihuia.mysterious.web.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2023/3/27 11:04 PM
 */

@RestController
@Api(tags = "节点管理")
@RequestMapping(value = "/node")
public class NodeController {

    @Autowired
    private INodeService nodeService;

    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public Response<Long> addNode(@RequestBody NodeParam nodeParam) {
        return ResponseUtil.buildSuccessResponse(nodeService.addNode(nodeParam, UserUtils.getCurrent()));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/update/{id}")
    public Response<Boolean> updateNode(@PathVariable Long id,
                                        @RequestBody NodeParam nodeParam) {
        return ResponseUtil.buildSuccessResponse(nodeService.updateNode(id, nodeParam, UserUtils.getCurrent()));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/getById/{id}")
    public Response<NodeVO> getById(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.getById(id));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteNode(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.deleteNode(id));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<NodeVO>> getNodeList(NodeQuery nodeQuery) {
        return ResponseUtil.buildSuccessResponse(nodeService.getNodeList(nodeQuery));
    }

    @ApiOperation("启用")
    @GetMapping(value = "/enable/{id}")
    public Response<Boolean> enableNode(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.enableNode(id, UserUtils.getCurrent()));
    }

    @ApiOperation("禁用")
    @GetMapping(value = "/disable/{id}")
    public Response<Boolean> disableNode(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.disableNode(id, UserUtils.getCurrent()));
    }
}
