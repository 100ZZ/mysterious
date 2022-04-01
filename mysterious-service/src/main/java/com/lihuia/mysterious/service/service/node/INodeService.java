package com.lihuia.mysterious.service.service.node;

import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:52 PM
 */

public interface INodeService {

    /**
     * 新增节点
     * @param nodeVO
     * @return
     */
    Long addNode(NodeVO nodeVO, UserVO userVO);

    /**
     * 更新节点
     * @param nodeVO
     * @param userVO
     * @return
     */
    Boolean updateNode(NodeVO nodeVO, UserVO userVO);

    /**
     * 查询已启用的所有slave节点列表
     * @return
     */
    List<NodeVO> getEnableNodeList();

    /**
     * 详情
     * @param id
     * @return
     */
    NodeVO getById(Long id);

    /**
     * 删除节点
     * @param id
     * @return
     */
    Boolean deleteNode(Long id);

    /**
     * 分页查询slave节点列表
     * @param name
     * @param host
     * @param page
     * @param size
     * @return
     */
    PageVO<NodeVO> getNodeList(String name, String host, Integer page, Integer size);

    /**
     * 启用slave节点（jmeter-server服务）
     * @param id
     */
    Boolean enableNode(Long id);

    /**
     * 禁用slave节点（jmeter-server服务）
     * @param id
     */
    Boolean disableNode(Long id);

    /**
     * 定时任务重启slave节点（jmeter-server服务）
     * @param id
     */
    Boolean cronReloadNode(Long id);

    /**
     * 重启slave节点（jmeter-server服务）
     * @param id
     */
    Boolean reloadNode(Long id);

    /**
     * 更新节点状态
     * @param id
     * @param status
     */
    Boolean updateNodeStatus(Long id, Integer status);
}
