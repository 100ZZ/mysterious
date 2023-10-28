package com.lihuia.mysterious.common.convert;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Slf4j
public class BeanConverter {

    public static <T> T doSingle(Object object, Class<T> classZz) {
        if (null == object) {
            return null;
        }
        try {
            T t = classZz.newInstance();
            BeanUtils.copyProperties(object, t);
            return t;
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SYSTEM_ERROR);
        }
    }
}
