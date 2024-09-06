package com.lihuia.mysterious.service.service.jmx.sample.http;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */


public interface IHttpService {

    /**
     * 根据jmxId获取http sample
     * @param jmxId
     * @return
     */
    HttpDO getByJmxId(Long jmxId);

    /**
     * 新增http sample
     * @param httpDO
     */
    void addHttp(HttpDO httpDO);

    /**
     * 更新http sample
     * @param httpDO
     */
    void updateHttp(HttpDO httpDO);

    /**
     * 删除
     * @param id
     */
    void deleteHttp(Long id);
}
