package com.lihuia.mysterious.core.vo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Data
@ApiModel
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 3232302395121415460L;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "每页数量")
    private Integer size;

    @ApiModelProperty(value = "总数")
    private Integer total;

    @ApiModelProperty(value = "列表")
    private List<T> list;

    public int getOffset(Integer page, Integer size) {
        this.size = size;
        this.page = page;
        this.total = 0;
        this.list = new ArrayList<>();

        Integer offset = 0;
        if (page == 0) {
            offset = 0;
        } else if (page >= 1) {
            offset = (page - 1) * size;
        }
        return offset;
    }
}
