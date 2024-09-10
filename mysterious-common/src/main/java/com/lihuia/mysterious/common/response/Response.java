package com.lihuia.mysterious.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Data
@Builder
public class Response<T> {

    private Integer code;
    private String message;
    private Boolean success;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime currentTime;
    private T data;
}
