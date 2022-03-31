package com.lihuia.mysterious.service.crud;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2022/3/31 11:41 PM
 */

@Component
public class CRUDEntity<T extends BaseDO> {

    /**
     * 每当做add操作，更新创建和更新者信息，每次更新者信息也刷新是因为分页会按照时间排序，因此不能为空
     * @param t
     * @param userVO
     */
    public void addT(T t, UserVO userVO) {
        t.setCreator(userVO.getUsername());
        t.setCreatorId(userVO.getId());
        t.setCreateTime(LocalDateTime.now());
        t.setModifier(userVO.getUsername());
        t.setModifierId(userVO.getId());
        t.setModifyTime(LocalDateTime.now());
    }

    /**
     * 没当做update操作，更新更新着信息
     * @param t
     * @param userVO
     */
    public void updateT(T t, UserVO userVO) {
        t.setId(userVO.getId());
        t.setModifier(userVO.getUsername());
        t.setModifierId(userVO.getId());
        t.setModifyTime(LocalDateTime.now());
    }}
