package com.lihuia.mysterious.common.exception;

import com.lihuia.mysterious.common.response.ResponseCodeEnum;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

public class MysteriousException extends RuntimeException {

    private static final long serialVersionUID = -1L;
    private ResponseCodeEnum responseCodeEnum = ResponseCodeEnum.FAIL;

    public MysteriousException() {
        super();
    }

    public MysteriousException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseCodeEnum.setMessage(message);
    }


    public MysteriousException(String message, Throwable cause) {
        super(message, cause);
        this.responseCodeEnum.setMessage(message);
    }

    public MysteriousException(String message) {
        super(message);
        this.responseCodeEnum.setMessage(message);
    }

    public MysteriousException(Throwable cause) {
        super(cause);
        this.responseCodeEnum.setMessage(cause.getMessage());

    }

    public MysteriousException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;

    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return this.responseCodeEnum;
    }

    @Override
    public String toString() {
        return "MysteriousException, [errorCode=" + this.responseCodeEnum.getCode() + ", errorMsg=" + this.responseCodeEnum.getMessage() + "]";
    }
}
