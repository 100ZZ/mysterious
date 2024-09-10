package com.lihuia.mysterious.core.vo.testcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午4:20
 */

@Data
@ApiModel
public class TestCaseParam {

    @ApiModelProperty(value = "用例名称")
    private String name;

    @ApiModelProperty(value = "用例描述")
    private String description;

    @ApiModelProperty(value = "业务线")
    private String biz;

    @ApiModelProperty(value = "服务")
    private String service;

    @ApiModelProperty(value = "版本号")
    private String version;
}
