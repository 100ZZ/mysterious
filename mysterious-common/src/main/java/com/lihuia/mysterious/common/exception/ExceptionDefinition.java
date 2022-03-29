package com.lihuia.mysterious.common.exception;

import java.io.Serializable;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:17 PM
 */

public class ExceptionDefinition implements Serializable {

    private static final long serialVersionUID = 2161872811104651733L;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    public ExceptionDefinition(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
