package com.lihuia.mysterious.common.exception;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:16 PM
 */
public interface BaseMessageEnums<T> {

    T getErrorCode();

    String getErrorMessage();
}
