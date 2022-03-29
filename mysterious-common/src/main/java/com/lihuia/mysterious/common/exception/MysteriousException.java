package com.lihuia.mysterious.common.exception;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:15 PM
 */

public class MysteriousException extends RuntimeException {

    private static final long serialVersionUID = -387693356417406549L;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;


    public MysteriousException() {
    }

    public MysteriousException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MysteriousException(String errorCode, String errorMessage) {
        super(String.format("异常代码：%s,异常信息：%s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public MysteriousException(ExceptionDefinition definition) {
        super(String.format("异常代码：%s,异常信息：%s", definition.getErrorCode(), definition.getErrorMessage()));
        this.errorCode = definition.getErrorCode();
        this.errorMessage = definition.getErrorMessage();
    }

    public MysteriousException(BaseMessageEnums<String> baseMessageEnums) {
        super(String.format("异常代码：%s,异常信息：%s", baseMessageEnums.getErrorCode(), baseMessageEnums.getErrorMessage()));
        this.errorCode = baseMessageEnums.getErrorCode();
        this.errorMessage = baseMessageEnums.getErrorMessage();
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
