package com.lihuia.mysterious.common.response;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:36 PM
 */

public class ApiResponse<T> extends Response {

    private static final long serialVersionUID = 782384891337929440L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> returnSuccess(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setData(data);
        apiResponse.setCode(DEFAULT_RETURN_SUCCESS_CODE);
        apiResponse.setT(System.currentTimeMillis());
        return apiResponse;
    }

    public static <T> ApiResponse<T> returnSuccess() {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(DEFAULT_RETURN_SUCCESS_CODE);
        apiResponse.setT(System.currentTimeMillis());
        return apiResponse;
    }

    public static <T> ApiResponse<T> returnError(String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setCode(DEFAULT_RETURN_ERROR_CODE);
        apiResponse.setMessage(message);
        apiResponse.setT(System.currentTimeMillis());
        return apiResponse;
    }

    public static <T> ApiResponse<T> returnDefaultError() {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setCode(DEFAULT_RETURN_ERROR_CODE);
        apiResponse.setMessage(DEFAULT_ERROR_MESSAGE);
        apiResponse.setT(System.currentTimeMillis());
        return apiResponse;
    }
}
