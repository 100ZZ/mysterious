package com.lihuia.mysterious.web.controller.node;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
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
 * @date 2022/3/27 11:04 PM
 */

@RestController
@Api(tags = "节点管理")
@RequestMapping(value = "/node")
public class NodeController {

    @Autowired
    private INodeService nodeService;

    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public Response<Long> addNode(@RequestBody NodeVO nodeVO) {
        return ResponseUtil.buildSuccessResponse(nodeService.addNode(nodeVO, UserUtils.getCurrent()));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/update")
    public Response<Boolean> updateNode(@RequestBody NodeVO nodeVO) {
        return ResponseUtil.buildSuccessResponse(nodeService.updateNode(nodeVO, UserUtils.getCurrent()));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/getById")
    public Response<NodeVO> getById(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.getById(id));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete")
    public Response<Boolean> deleteNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.deleteNode(id));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<NodeVO>> getNodeList(NodeQuery nodeQuery) {
        return ResponseUtil.buildSuccessResponse(nodeService.getNodeList(nodeQuery));
    }

    @ApiOperation("启用")
    @GetMapping(value = "/enable")
    public Response<Boolean> enableNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.enableNode(id));
    }

    @ApiOperation("禁用")
    @GetMapping(value = "/disable")
    public Response<Boolean> disableNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.disableNode(id));
    }
}
