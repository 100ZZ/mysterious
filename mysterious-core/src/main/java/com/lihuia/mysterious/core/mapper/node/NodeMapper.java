package com.lihuia.mysterious.core.mapper.node;

import com.lihuia.mysterious.core.mapper.base.BaseMapper;
import com.lihuia.mysterious.core.entity.node.NodeDO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 8:15 PM
 */

public interface NodeMapper extends BaseMapper<NodeDO> {

    /**
     * 获取启用中的节点
     * @param type
     * @param status
     * @return
     */
    List<NodeDO> getEnableNodeList(Integer type, Integer status);

}
