package com.lihuia.mysterious.core.entity.jmx.sample.csv;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/10/10 16:38
 */

@Data
public class CsvDataDO extends BaseDO<Long> {
    /**
     * 用例ID
     */
    private Long testCaseId;

    /**
     * 脚本ID
     */
    private Long jmxId;

    /**
     * CSV文件名
     */
    private String filename;

    /**
     * 文件编码
     */
    private String fileEncoding;

    /**
     * 变量名（逗号分隔）
     */
    private String variableNames;

    /**
     * 是否忽略第一行；1-是，0-否
     */
    private Integer ignoreFirstLine;

    /**
     * 分隔符
     */
    private String delimiter;

    /**
     * 允许带引号的数据；1-是，0-否
     */
    private Integer allowQuotedData;

    /**
     * 是否在EOF时循环；1-是，0-否
     */
    private Integer recycleOnEof;

    /**
     * 是否在EOF时停止线程；1-是，0-否
     */
    private Integer stopThreadOnEof;

    /**
     * 共享模式（all, group, thread）
     */
    private String sharingMode;
}
