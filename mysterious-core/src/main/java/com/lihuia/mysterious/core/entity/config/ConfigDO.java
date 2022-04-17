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

    /** 配置字段 */
    private String configKey;

    /** 字段值 */
    private String configValue;

    /** 字段描述 */
    private String description;
}
