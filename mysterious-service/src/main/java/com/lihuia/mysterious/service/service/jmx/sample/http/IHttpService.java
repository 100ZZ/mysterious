package com.lihuia.mysterious.service.service.jmx.sample.http;

import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpVO;

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
    HttpVO getByJmxId(Long jmxId);

    /**
     * 新增http sample
     * @param httpVO
     */
    void addHttp(HttpVO httpVO);

    /**
     * 更新http sample
     * @param httpVO
     */
    void updateHttp(HttpVO httpVO);

    /**
     * 删除
     * @param id
     */
    void deleteHttp(Long id);
}
