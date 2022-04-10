package com.lihuia.mysterious.core.vo.report;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/10 9:23 PM
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ReportByTestCaseQuery extends BaseQuery {


    @ApiModelProperty("用例名称")
    private String name;

    @ApiModelProperty("用例ID")
    private Long testCaseId;

    @ApiModelProperty("创建人")
    private Long creatorId;
}
