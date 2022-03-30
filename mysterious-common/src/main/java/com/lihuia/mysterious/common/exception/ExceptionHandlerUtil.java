package com.lihuia.mysterious.common.exception;

import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.response.ResponseStatus;
import com.lihuia.mysterious.common.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerUtil {
    /**
     * 格式化异常
     *
     * @param ex
     * @param logInfo
     * @return
     */
    private ResponseStatus formatException(Exception ex, String logInfo) {
        log.warn(logInfo);
        //log.warn(StringUtil.getStackTraceAsString(ex));
        ResponseCodeEnum.SYSTEM_ERROR.setMessage(logInfo);
        return ResponseUtil.buildResponseStatus(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 格式化异常信息
     *
     * @param ex
     * @param logInfo
     * @param responseCodeEnum
     * @return
     */
    private ResponseStatus formatException(Exception ex, String logInfo, ResponseCodeEnum responseCodeEnum) {
        log.warn(logInfo);
        //log.warn(StringUtil.getStackTraceAsString(ex));
        responseCodeEnum.setMessage(logInfo);
        return ResponseUtil.buildResponseStatus(responseCodeEnum);
    }

    // json 解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String logInfo = String.format("接口参数解析失败,请检查参数格式, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    @ExceptionHandler(BusinessException.class)
    public Object businessException(BusinessException ex) {
        return formatException(ex, ex.getMessage());
    }

    // 运行时异常
    @ExceptionHandler(value = RuntimeException.class)
    public Object runtimeException(RuntimeException ex) {
        log.info("系统运行异常, 异常信息:", ex);
        String logInfo = String.format("系统运行异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    // 空指针异常
    @ExceptionHandler(value = NullPointerException.class)
    public Object nullPointerException(NullPointerException ex) {
        log.info("系统运行异常, 异常信息:", ex);
        String logInfo = String.format("空指针异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    // sql 主键冲突异常
    @ExceptionHandler(value = DuplicateKeyException.class)
    public Object duplicateKeyException(DuplicateKeyException ex) {
        String logInfo = String.format("SQL主键冲突异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    // 中断异常
    @ExceptionHandler(value = InterruptedException.class)
    public Object interruptedException(InterruptedException ex) {
        String logInfo = String.format("中断异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    // json异常
    @ExceptionHandler(value = JSONException.class)
    public Object jsonException(JSONException ex) {
        String logInfo = String.format("JSON异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    // cps自定义异常
    @ExceptionHandler(value = MysteriousException.class)
    public Object jsonException(MysteriousException ex) {
        String logInfo = String.format("接口测试平台异常, 异常信息: %s", ex.getMessage());
        if (ex.getResponseCodeEnum() != null && ex.getResponseCodeEnum() != ResponseCodeEnum.SYSTEM_ERROR) {
            return formatException(ex, ex.getMessage(), ResponseCodeEnum.FAIL);

        } else {
            return formatException(ex, logInfo);
        }
    }

    /** http请求参数缺失 */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Object missingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        String logInfo = String.format("请求参数缺失, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    /** http请求的方法不正确 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object httpRequestMethodNotSupportedExceptionHandler(
            HttpRequestMethodNotSupportedException ex) {

        String logInfo = String.format("Http请求方法不正确, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    /** 请求参数类型不正确 */
    @ExceptionHandler(TypeMismatchException.class)
    public Object typeMismatchExceptionHandler(TypeMismatchException ex) {

        String logInfo = String.format("Http请求参数类型不正确, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    /** 数据格式不正确 */
    @ExceptionHandler(DataFormatException.class)
    public Object dataFormatExceptionHandler(DataFormatException ex) {

        String logInfo = String.format("数据格式不正确, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

    /**
     * 编码格式异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = UnsupportedEncodingException.class)
    public Object handleEncodingException(UnsupportedEncodingException e) {
        String logInfo = String.format("编码不正确, 异常信息: %s", e.getMessage());
        return formatException(e, logInfo);
    }

    // 其他异常
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception ex) {
        String logInfo = String.format("系统发生异常, 异常信息: %s", ex.getMessage());
        return formatException(ex, logInfo);
    }

}
