package com.lihuia.mysterious.common.convert;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

@Slf4j
public class BeanConverter {

    public static <T> T doSingle(Object object, Class<T> classZz) {
        if (null == object) {
            return null;
        }
        try {
            T t = classZz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(object, t);
            return t;
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SYSTEM_ERROR);
        }
    }
}
