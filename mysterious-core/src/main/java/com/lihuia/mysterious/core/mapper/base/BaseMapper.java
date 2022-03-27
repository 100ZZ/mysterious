package com.lihuia.mysterious.core.mapper.base;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 8:14 PM
 */

public interface BaseMapper<T> {

    void add(T t);

    T getById(Long id);

    void batchAdd(List<T> list);

    int update(T t);

    int batchUpdate(List<T> list);

    int delete(Long id);

    int batchDelete(List<Long> list);
}
