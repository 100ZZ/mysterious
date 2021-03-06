package com.lihuia.mysterious.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

@Data
@Builder
public class Response<T> {

    private Integer code;
    private String message;
    private Boolean success;
    private Long currentTime;
    private T data;
}
