package com.lihuia.mysterious.service.service.user.impl;

import com.lihuia.mysterious.common.convert.CommonBeanConverter;
import com.lihuia.mysterious.core.domain.user.UserVO;
import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.user.UserMapper;
import com.lihuia.mysterious.service.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:15 PM
 */

@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long addUser(UserVO userVO) {
        UserDO userDO = CommonBeanConverter.doSingle(userVO, UserDO.class);
        userMapper.add(userDO);
        return userDO.getId();
    }

    @Override
    public Integer deleteUser(Long id) {
        return null;
    }

    @Override
    public Integer updateUser(UserVO userVO) {
        return null;
    }

    @Override
    public UserVO getById(Long id) {
        return null;
    }
}
