package com.lihuia.mysterious.web.controller.node;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.service.service.node.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lihuia.com
 * @date 2022/3/27 11:04 PM
 */

@RestController
@RequestMapping(value = "/node")
public class NodeController {

    @Autowired
    private INodeService nodeService;

    @GetMapping(value = "/getById")
    public Response<NodeVO> getById(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(nodeService.getById(id));
    }
}
