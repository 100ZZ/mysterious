package com.lihuia.mysterious.service.service.jmx.sample.dubbo.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboAttachmentArgsDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.dubbo.DubboAttachmentArgsMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboAttachmentArgsVO;
import com.lihuia.mysterious.service.service.jmx.sample.dubbo.IDubboAttachmentArgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:24
 */

@Service
public class DubboAttachmentArgsService implements IDubboAttachmentArgsService {

    @Autowired
    private DubboAttachmentArgsMapper dubboAttachmentArgsMapper;

    @Override
    public void addDubboAttachmentArgs(DubboAttachmentArgsVO dubboAttachmentArgsVO) {
        dubboAttachmentArgsMapper.add(BeanConverter.doSingle(dubboAttachmentArgsVO, DubboAttachmentArgsDO.class));
    }

    @Override
    public void batchDeleteDubboAttachmentArgs(List<Long> ids) {
        dubboAttachmentArgsMapper.batchDelete(ids);
    }

    @Override
    public List<DubboAttachmentArgsVO> getListByDubboId(Long dubboId) {
        return BeanConverter.doList(dubboAttachmentArgsMapper.getListByDubboId(dubboId), DubboAttachmentArgsVO.class);
    }

    @Override
    public List<DubboAttachmentArgsVO> getListByJmxId(Long jmxId) {
        return BeanConverter.doList(dubboAttachmentArgsMapper.getListByJmxId(jmxId), DubboAttachmentArgsVO.class);
    }

    @Override
    public void deleteDubboAttachmentArgs(Long id) {
        dubboAttachmentArgsMapper.delete(id);
    }

    @Override
    public List<DubboAttachmentArgsVO> getExistAttachmentArgsList(Long dubboId, String attachmentKey) {
        return BeanConverter.doList(dubboAttachmentArgsMapper.getExistAttachmentList(dubboId, attachmentKey), DubboAttachmentArgsVO.class);
    }
}
