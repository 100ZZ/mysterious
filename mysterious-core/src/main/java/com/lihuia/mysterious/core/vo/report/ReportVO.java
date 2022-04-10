package com.lihuia.mysterious.core.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/5 4:30 PM
 */

@Data
@ApiModel
public class ReportVO {

    @ApiModelProperty("报告编号")
    private Long id;

    @ApiModelProperty("报告名称")
    private String name;

    @ApiModelProperty("报告描述")
    private String description;

    @ApiModelProperty("用例ID")
    private Long testCaseId;

    @ApiModelProperty("报告路径")
    private String reportDir;

    @ApiModelProperty("执行类型，1-调试；2-执行")
    private Integer execType;

    @ApiModelProperty("执行状态; 0-未执行，1-执行中, 2-执行成功, 3-执行异常")
    private Integer status;

    @ApiModelProperty("调试的返回值")
    private String responseData;

    @ApiModelProperty("jmeter.log目录")
    private String jmeterLogFilePath;
}
