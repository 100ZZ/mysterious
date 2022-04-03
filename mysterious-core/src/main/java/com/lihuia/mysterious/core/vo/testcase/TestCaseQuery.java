package com.lihuia.mysterious.core.vo.testcase;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:20
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class TestCaseQuery extends BaseQuery {

    @ApiModelProperty("用例编号")
    private Long id;

    @ApiModelProperty("用例名称")
    private String name;

    @ApiModelProperty("业务线")
    private String biz;

    @ApiModelProperty("服务")
    private String service;

    @ApiModelProperty("创建人")
    private Long creatorId;
}
