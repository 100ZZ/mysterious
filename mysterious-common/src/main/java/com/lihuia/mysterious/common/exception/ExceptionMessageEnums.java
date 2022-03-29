package com.lihuia.mysterious.common.exception;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:17 PM
 */

public enum ExceptionMessageEnums implements BaseMessageEnums<String> {

    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),

    /**
     * rpc服务调用异常 相关报错信息
     */
    RPC_ERROR("RPC_ERROR", "rpc服务调用异常!"),

    /**
     * EXECUTE_ERROR 执行异常
     */
    EXECUTE_ERROR("EXECUTE_ERROR", "执行异常！"),

    /**
     * PARAM_ERROR 非法参数
     */
    PARAM_ERROR("PARAM_ERROR", "非法参数！"),

    /**
     * PARAM_MISSING 参数缺失
     */
    PARAM_MISSING("PARAM_MISSING", "参数缺失！"),

    /**
     * page, size值不正确
     */
    PAGE_INFO_ERROR("PAGE_INFO_ERROR", "page, size值不正确"),

    /**
     * 结果未定义
     */
    RESULT_NOT_DEFINED_ERROR("RESULT_NOT_DEFINED_ERROR", "the correct return value is not defined");

    private String errorCode;

    private String errorMessage;

    ExceptionMessageEnums(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
