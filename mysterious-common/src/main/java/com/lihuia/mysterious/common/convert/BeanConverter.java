package com.lihuia.mysterious.common.convert;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2023/3/29 11:02 PM
 */

@Slf4j
public class BeanConverter {

    public static <T> T doSingle(Object object, Class<T> classZz) {
        if (object == null) {
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

    public static <T> List<T> doList(List<?> objects, Class<T> classZz) {
        if (objects == null || objects.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objects.stream()
                    .map(obj -> doSingle(obj, classZz))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SYSTEM_ERROR);
        }
    }
}
