package com.lihuia.mysterious.core.vo.csv;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午4:10
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CsvVO extends BaseVO<Long> {

    @ApiModelProperty(value = "上传前CSV文件名称")
    private String srcName;

    @ApiModelProperty(value = "上传后CSV文件名称")
    private String dstName;

    @ApiModelProperty(value = "CSV文件描述")
    private String description;

    @ApiModelProperty(value = "CSV文件路径")
    private String csvDir;

    @ApiModelProperty(value = "CSV文件关联的用例")
    private Long testCaseId;
}
