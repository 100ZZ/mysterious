package com.lihuia.mysterious.core.entity.config;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 配置字段 */
    private String key;

    /** 字段值 */
    private String value;

    /** 字段描述 */
    private String description;
}
