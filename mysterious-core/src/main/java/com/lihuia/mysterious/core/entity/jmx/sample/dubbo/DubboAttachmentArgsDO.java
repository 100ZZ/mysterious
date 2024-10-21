package com.lihuia.mysterious.core.entity.jmx.sample.dubbo;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:00
 */

@Data
public class DubboAttachmentArgsDO extends BaseDO<Long> {

    /** 用例ID */
    private Long testCaseId;

    /** 脚本ID */
    private Long jmxId;

    /** Dubbo请求ID */
    private Long dubboId;

    /** 附件键 */
    private String attachmentKey;

    /** 附件值 */
    private String attachmentValue;
}
