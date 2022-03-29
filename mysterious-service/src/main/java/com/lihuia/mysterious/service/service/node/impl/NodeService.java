package com.lihuia.mysterious.service.service.node.impl;

import com.lihuia.mysterious.core.mapper.node.NodeMapper;
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.NodeTypeEnum;
import com.lihuia.mysterious.service.service.node.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:55 PM
 */

@Service
public class NodeService implements INodeService {

    @Autowired
    private NodeMapper nodeMapper;

    @Override
    public List<NodeDO> getEnableNodeList() {
        return nodeMapper.getEnableNodeList(NodeTypeEnum.SLAVE.getCode(), NodeStatusEnum.ENABLE.getCode());
    }

    @Override
    public NodeDO getById(Long id) {
        return nodeMapper.getById(id);
    }
}
