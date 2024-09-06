package com.lihuia.mysterious.service.service.jmx.sample.http;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IHttpHeaderService {

    /**
     * 添加http header
     * @param httpHeaderDO
     */
    void addHttpHeader(HttpHeaderDO httpHeaderDO);

    /**
     * 批量添加http header
     * @param httpHeaderDOList
     */
    void batchAddHttpHeader(List<HttpHeaderDO> httpHeaderDOList);

    /**
     * 根据http id获取所有的header
     * @param httpId
     * @return
     */
    List<HttpHeaderDO> getListByHttpId(Long httpId);

    /**
     * 根据jmx id获取所有的header
     * @param jmxId
     * @return
     */
    List<HttpHeaderDO> getListByJmxId(Long jmxId);

    /**
     * 更新http header
     * @param httpHeaderDO
     */
    void updateHttpHeader(HttpHeaderDO httpHeaderDO);

    /**
     * 批量更新
     * @param httpHeaderDOList
     */
    void batchUpdateHttpHeader(List<HttpHeaderDO> httpHeaderDOList);

    /**
     * 删除
     * @param id
     */
    void deleteHttpHeader(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDeleteHttpHeader(List<Long> ids);

    /**
     * name + value是否已存在
     * @param httpId
     * @param name
     * @return
     */
    List<HttpHeaderDO> getExistHeaderList(Long httpId, String name);
}
