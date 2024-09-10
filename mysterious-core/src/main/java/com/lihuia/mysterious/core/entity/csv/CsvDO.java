package com.lihuia.mysterious.core.entity.csv;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:18
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CsvDO extends BaseDO<Long> {

    /** 上传前CSV文件名称 */
    private String srcName;

    /** 上传后CSV文件名称 */
    private String dstName;

    /** CSV文件描述 */
    private String description;

    /** CSV文件路径 */
    private String csvDir;

    /** CSV文件关联的用例 */
    private Long testCaseId;
}
