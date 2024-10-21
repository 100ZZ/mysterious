package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpParamDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.http.HttpParamMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpParamVO;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class HttpParamService implements IHttpParamService {

    @Autowired
    private HttpParamMapper httpParamMapper;

    @Override
    public void addHttpParam(HttpParamVO httpParamVO) {
        httpParamMapper.add(BeanConverter.doSingle(httpParamVO, HttpParamDO.class));
    }

    @Override
    public void batchAddHttpParam(List<HttpParamVO> httpParamVOList) {
        httpParamMapper.batchAdd(BeanConverter.doList(httpParamVOList, HttpParamDO.class));
    }

    @Override
    public List<HttpParamVO> getListByHttpId(Long httpId) {
        return BeanConverter.doList(httpParamMapper.getListByHttpId(httpId), HttpParamVO.class);
    }

    @Override
    public List<HttpParamVO> getListByJmxId(Long jmxId) {
        return BeanConverter.doList(httpParamMapper.getListByJmxId(jmxId), HttpParamVO.class);
    }

    @Override
    public void updateHttpParam(HttpParamVO httpParamVO) {
        httpParamMapper.update(BeanConverter.doSingle(httpParamVO, HttpParamDO.class));
    }

    @Override
    public void batchUpdateHttpParam(List<HttpParamVO> httpParamVOList) {
        httpParamMapper.batchUpdate(BeanConverter.doList(httpParamVOList, HttpParamDO.class));
    }

    @Override
    public void deleteHttpParam(Long id) {
        httpParamMapper.delete(id);
    }

    @Override
    public void batchDeleteHttpParam(List<Long> ids) {
        httpParamMapper.batchDelete(ids);
    }

    @Override
    public List<HttpParamVO> getExistParamList(Long httpId, String paramKey) {
        return BeanConverter.doList(httpParamMapper.getExistParamList(httpId, paramKey), HttpParamVO.class);
    }
}
