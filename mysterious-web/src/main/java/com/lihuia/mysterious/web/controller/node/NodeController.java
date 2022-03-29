package com.lihuia.mysterious.web.controller.node;

import com.lihuia.mysterious.service.service.node.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lihuia.com
 * @date 2022/3/27 11:04 PM
 */

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private INodeService nodeService;
}
