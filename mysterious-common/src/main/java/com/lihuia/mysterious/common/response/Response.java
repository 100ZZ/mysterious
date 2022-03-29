package com.lihuia.mysterious.common.response;

import java.io.Serializable;

/**
 * @author lihuia.com
 * @date 2022/3/27 6:07 PM
 */

public class Response implements Serializable {

    private static final long serialVersionUID = 1431488354476457871L;

    /**
     * 默认统一成功返回code
     */
    protected static final Integer DEFAULT_RETURN_SUCCESS_CODE = 0;

    /**
     * 默认统一失败返回code
     */
    protected static final Integer DEFAULT_RETURN_ERROR_CODE = -1;


    /**
     * 默认统一系统异常代码
     */
    protected static final String DEFAULT_ERROR_CODE = "NETWORK_ERROR";

    /**
     * 默认统一系统异常文案
     */
    protected static final String DEFAULT_ERROR_MESSAGE = "网络异常，请稍后重试";

    /**
     * 成功失败标志
     */
    private boolean success = true;

    /**
     * 错误代码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 时间戳
     */
    private Long t = System.currentTimeMillis();

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
