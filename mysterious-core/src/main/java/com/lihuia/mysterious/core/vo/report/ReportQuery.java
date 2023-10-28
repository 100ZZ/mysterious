package com.lihuia.mysterious.core.vo.report;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/10 7:50 PM
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ReportQuery extends BaseQuery {

    @ApiModelProperty(value = "用例名称")
    private String name;

    @ApiModelProperty(value = "创建人")
    private Long creatorId;
}
