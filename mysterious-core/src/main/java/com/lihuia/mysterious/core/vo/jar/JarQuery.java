package com.lihuia.mysterious.core.vo.jar;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午4:14
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class JarQuery extends BaseQuery {

    @ApiModelProperty(value = "上传前JAR文件名称")
    private String srcName;

    @ApiModelProperty(value = "JAR文件关联的用例")
    private Long testCaseId;

    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;
}
