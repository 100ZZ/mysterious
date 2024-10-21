package com.lihuia.mysterious.core.mapper.jmx.sample.dubbo;

import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboAttachmentArgsDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:03
 */
public interface DubboAttachmentArgsMapper extends BaseMapper<DubboAttachmentArgsDO> {

    List<DubboAttachmentArgsDO> getListByDubboId(Long dubboId);

    List<DubboAttachmentArgsDO> getListByJmxId(Long jmxId);
    List<DubboAttachmentArgsDO> getExistAttachmentList(Long dubboId, String attachmentKey);
}
