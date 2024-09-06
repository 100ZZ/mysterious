package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class HttpService implements IHttpService {

    @Autowired
    private HttpMapper httpMapper;

    @Override
    public HttpDO getByJmxId(Long jmxId) {
        return httpMapper.getByJmxId(jmxId);
    }

    @Override
    public void addHttp(HttpDO httpDO) {
        httpMapper.add(httpDO);
    }

    @Override
    public void updateHttp(HttpDO httpDO) {
        httpMapper.update(httpDO);
    }

    @Override
    public void deleteHttp(Long id) {
        httpMapper.delete(id);
    }
}
