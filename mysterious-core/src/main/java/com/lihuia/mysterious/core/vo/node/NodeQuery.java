package com.lihuia.mysterious.core.vo.node;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午2:51
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class NodeQuery extends BaseQuery {

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点地址")
    private String host;
}
