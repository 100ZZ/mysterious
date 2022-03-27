package com.lihuia.mysterious.common.response;

/**
 * @author lihuia.com
 * @date 2022/3/27 6:07 PM
 */

public class ResponseUtil {

    /**
     * 返回成功状态，包含数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> buildSuccessResponse(T data){
        return buildResponse(data, ResponseCode.SUCCESS);
    }

    /**
     * 返回成功状态，不包含数据
     * @return
     */
    public static ResponseStatus success(){
        return buildResponseStatus(ResponseCode.SUCCESS);
    }

    /**
     * 返回失败状态
     * @param responseCode
     * @return
     */
    public static ResponseStatus buildFailResponse(ResponseCode responseCode){
        ResponseCode.FAIL.setMessage(responseCode.getMessage());
        return buildResponseStatus(ResponseCode.FAIL);
    }

    /**
     * 返回Response，包含数据
     * @param data
     * @param responseCode
     * @return
     */
    public static Response buildResponse(Object data,ResponseCode responseCode){
        return Response.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .isSuccess(responseCode.getSuccess())
                .t(System.currentTimeMillis())
                .data(data)
                .build();
    }

    /**
     * 返回success Response，包含数据
     * @param data
     * @return
     */
    public static Response genSuccessResponse(Object data){
        ResponseCode resp = ResponseCode.SUCCESS;
        return Response.builder()
                .code(resp.getCode())
                .message(resp.getMessage())
                .isSuccess(resp.getSuccess())
                .t(System.currentTimeMillis())
                .data(data)
                .build();
    }

    /**
     * 返回状态ResponseStatus
     * @param responseCode
     * @return
     */
    public static ResponseStatus buildResponseStatus(ResponseCode responseCode){
        return ResponseStatus.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .isSuccess(responseCode.getSuccess())
                .t(System.currentTimeMillis())
                .build();
    }
}
