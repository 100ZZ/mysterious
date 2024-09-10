package com.lihuia.mysterious.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Slf4j
@Data
@EqualsAndHashCode(callSuper=true)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 572193279810657156L;

    /** 非法字符错误, new HttpPost(url) 中报出来的 **/
    private static final String ILLEGAL_CHAR = "Illegal character in path at index ";

    private String errorCode = "SYSTEM_ERROR";

    private String errorMsg = "";

    public BusinessException() {
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = packMsg(message);
    }

    public BusinessException(String message) {
        super(message);
        this.errorMsg = packMsg(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.errorMsg = packMsg(cause.getMessage());
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = packMsg(message);
    }

    @Override
    public String toString() {
        return "BusinessException [errorCode=" + this.errorCode + ", errorMsg=" + this.errorMsg + "]";
    }

    private String packMsg(String errorMsg) {
        if (errorMsg.contains(ILLEGAL_CHAR)) {
            return errorMsg.replace(ILLEGAL_CHAR, "字符串存在空格等特殊字符, 列数");
        }
        return errorMsg;
    }
}
