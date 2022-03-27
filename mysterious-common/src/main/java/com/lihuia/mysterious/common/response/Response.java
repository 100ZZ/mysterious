package com.lihuia.mysterious.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/27 6:07 PM
 */

@Data
@Builder
public class Response<T> {

    private Integer code;

    private String message;

    private Boolean isSuccess;

    private Long t;//时间戳

    private T data;
}
