package com.lihuia.mysterious.core.vo.csv;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:10
 */

@Data
@ApiModel
public class CsvVO {

    @ApiModelProperty("配置编号")
    private Long id;

    @ApiModelProperty("上传前CSV文件名称")
    private String srcName;

    @ApiModelProperty("上传后CSV文件名称")
    private String dstName;

    @ApiModelProperty("CSV文件描述")
    private String description;

    @ApiModelProperty("CSV文件路径")
    private String csvDir;

    @ApiModelProperty("CSV文件关联的用例")
    private Long testCaseId;
}
