package com.lihuia.mysterious.common.convert;

import com.lihuia.mysterious.common.exception.ExceptionMessageEnums;
import com.lihuia.mysterious.common.exception.MysteriousException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

@Slf4j
public class CommonBeanConverter {

    public static <T> T doSingle(Object object, Class<T> classZz) {
        try {
            T t = classZz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(object, t);
            return t;
        } catch (Exception e) {
            throw new MysteriousException(ExceptionMessageEnums.SYSTEM_ERROR);
        }
    }

    public static <T> List<T> doList(List<?> objectList, Class<T> classZz) {
        if (CollectionUtils.isEmpty(objectList)) {
            return Collections.emptyList();
        }
        return objectList.stream()
                .map(value -> doSingle(value, classZz))
                .collect(Collectors.toList());
    }
}
