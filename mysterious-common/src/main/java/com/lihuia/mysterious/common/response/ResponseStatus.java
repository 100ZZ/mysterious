package com.lihuia.mysterious.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Data
@Builder
public class ResponseStatus {

    private Integer code;
    private String message;
    private Boolean success;
    private Long currentTime;
}
