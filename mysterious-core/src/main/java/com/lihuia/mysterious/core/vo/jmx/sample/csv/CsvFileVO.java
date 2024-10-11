package com.lihuia.mysterious.core.vo.jmx.sample.csv;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/10/11 14:36
 */

@Data
@ApiModel
public class CsvFileVO {

    /**
     * CSV文件名
     */
    @ApiModelProperty(value = "CSV文件名")
    private String filename;

    /**
     * 变量名（逗号分隔）
     */
    @ApiModelProperty(value = "变量名（逗号分隔）")
    private String variableNames;

    /**
     * 分隔符
     */
    @ApiModelProperty(value = "分隔符")
    private String delimiter;
}
