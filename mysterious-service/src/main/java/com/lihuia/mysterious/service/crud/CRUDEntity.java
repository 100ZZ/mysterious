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

    public void addT(T t, UserVO userVO) {
        t.setCreator(userVO.getUsername());
        t.setCreatorId(userVO.getId());
        t.setCreateTime(LocalDateTime.now());
        t.setModifier(userVO.getUsername());
        t.setModifierId(userVO.getId());
        t.setModifyTime(LocalDateTime.now());
    }

    public void updateT(T t, UserVO userVO) {
        t.setId(userVO.getId());
        t.setModifier(userVO.getUsername());
        t.setModifierId(userVO.getId());
        t.setModifyTime(LocalDateTime.now());
    }}
