package com.lihuia.mysterious.web.controller.node;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.node.INodeService;
import com.lihuia.mysterious.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2022/3/27 11:04 PM
 */

@RestController
@RequestMapping(value = "/node")
public class NodeController {

    @Autowired
    private INodeService nodeService;

    @PostMapping(value = "/add")
    public Response<Long> addNode(@RequestBody NodeVO nodeVO) {
        return ResponseUtil.buildSuccessResponse(nodeService.addNode(nodeVO, UserUtils.getCurrent()));
    }

    @PostMapping(value = "/update")
    public Response<Boolean> updateNode(@RequestBody NodeVO nodeVO) {
        return ResponseUtil.buildSuccessResponse(nodeService.updateNode(nodeVO, UserUtils.getCurrent()));
    }

    @GetMapping(value = "/getById")
    public Response<NodeVO> getById(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.getById(id));
    }

    @GetMapping(value = "/delete")
    public Response<Boolean> deleteNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.deleteNode(id));
    }

    @GetMapping(value = "/list")
    public Response<PageVO<NodeVO>> getNodeList(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "host", required = false) String host,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "size") Integer size) {
        return ResponseUtil.buildSuccessResponse(nodeService.getNodeList(name, host, page, size));
    }

    @GetMapping(value = "/enable")
    public Response<Boolean> enableNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.enableNode(id));
    }

    @GetMapping(value = "/disable")
    public Response<Boolean> disableNode(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.disableNode(id));
    }
}
