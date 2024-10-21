package com.lihuia.mysterious.service.service.jmx.sample.dubbo;

import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboAttachmentArgsVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:23
 */
public interface IDubboAttachmentArgsService {

    void addDubboAttachmentArgs(DubboAttachmentArgsVO dubboAttachmentArgsVO);

    /**
     * 批量删除
     * @param ids
     */
    void batchDeleteDubboAttachmentArgs(List<Long> ids);

    List<DubboAttachmentArgsVO> getListByDubboId(Long dubboId);

    List<DubboAttachmentArgsVO> getListByJmxId(Long jmxId);

    void deleteDubboAttachmentArgs(Long id);

    List<DubboAttachmentArgsVO> getExistAttachmentArgsList(Long dubboId, String attachmentKey);
}
