package com.lihuia.mysterious.core.vo.jmx;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:14
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class JmxQuery extends BaseQuery {

    @ApiModelProperty(value = "上传前JMX文件名称")
    private String srcName;

    @ApiModelProperty(value = "JMX文件关联的用例")
    private Long testCaseId;

    @ApiModelProperty(value = "创建人ID ")
    private Long creatorId;
}
