package com.lihuia.mysterious.service.service.node.impl;

import com.lihuia.mysterious.common.convert.CommonBeanConverter;
import com.lihuia.mysterious.core.mapper.node.NodeMapper;
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.NodeTypeEnum;
import com.lihuia.mysterious.service.service.node.INodeService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<NodeVO> getEnableNodeList() {
        return new ArrayList<>();
    }

    @Override
    public NodeVO getById(Long id) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            return null;
        }
        NodeVO nodeVO = CommonBeanConverter.doSingle(nodeDO, NodeVO.class);
        nodeVO.setId(id);
        return nodeVO;
    }
}
