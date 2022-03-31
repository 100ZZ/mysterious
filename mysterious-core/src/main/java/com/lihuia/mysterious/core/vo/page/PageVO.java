package com.lihuia.mysterious.core.vo.page;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 3232302395121415460L;

    private Integer page;
    private Integer size;
    private Integer total;
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
