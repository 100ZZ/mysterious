package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.http.HttpMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpVO;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpService;
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
    public HttpVO getByJmxId(Long jmxId) {
        return BeanConverter.doSingle(httpMapper.getByJmxId(jmxId), HttpVO.class);
    }

    @Override
    public void addHttp(HttpVO httpVO) {
        httpMapper.add(BeanConverter.doSingle(httpVO, HttpDO.class));
    }

    @Override
    public void updateHttp(HttpVO httpVO) {
        httpMapper.update(BeanConverter.doSingle(httpVO, HttpDO.class));
    }

    @Override
    public void deleteHttp(Long id) {
        httpMapper.delete(id);
    }
}
