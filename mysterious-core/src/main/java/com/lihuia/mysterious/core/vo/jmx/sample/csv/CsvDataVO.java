package com.lihuia.mysterious.core.vo.jmx.sample.csv;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/10 17:06
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CsvDataVO extends BaseVO<Long> {

    /**
     * 关联的用例
     */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /**
     * 关联的JMX脚本
     */
    @ApiModelProperty(value = "关联的JMX脚本")
    private Long jmxId;

    /**
     * 文件编码
     */
    @ApiModelProperty(value = "文件编码")
    private String fileEncoding;

    /**
     * 是否忽略第一行；1-是，0-否
     */
    @ApiModelProperty(value = "是否忽略第一行；1-是，0-否")
    private Integer ignoreFirstLine;

    /**
     * 允许带引号的数据；1-是，0-否
     */
    @ApiModelProperty(value = "允许带引号的数据；1-是，0-否")
    private Integer allowQuotedData;

    /**
     * 是否在EOF时循环；1-是，0-否
     */
    @ApiModelProperty(value = "是否在EOF时循环；1-是，0-否")
    private Integer recycleOnEof;

    /**
     * 是否在EOF时停止线程；1-是，0-否
     */
    @ApiModelProperty(value = "是否在EOF时停止线程；1-是，0-否")
    private Integer stopThreadOnEof;

    /**
     * 共享模式（all, group, thread）
     */
    @ApiModelProperty(value = "共享模式（all, group, thread）")
    private String sharingMode;

    /**
     * CSV文件信息列表
     */
    private List<CsvFileVO> csvFileVOList;
}
