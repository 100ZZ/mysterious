package com.lihuia.mysterious.service.service.user.impl;

import com.lihuia.mysterious.common.convert.CommonBeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.user.UserMapper;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:15 PM
 */

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /** token有效时间12小时 */
    private final static int EXPIRE_TIME = 12;

    private void checkUserParam(UserVO userVO) {
        if (ObjectUtils.isEmpty(userVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isBlank(userVO.getUsername()) || StringUtils.isBlank(userVO.getPassword())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkUserExist(UserVO userVO) {
        String username = userVO.getUsername();
        UserDO userDO = userMapper.getByUsername(username);
        if (ObjectUtils.isNotEmpty(userDO)) {
            throw new MysteriousException(ResponseCodeEnum.USER_EXIST);
        }
    }

    private void refreshToken(UserDO userDO) {
        /** 新增和更新用户，都会刷新token */
        String token = UUID.randomUUID().toString();
        LocalDateTime effectTime = LocalDateTime.now();
        LocalDateTime expireTime = effectTime.plusHours(EXPIRE_TIME);
        userDO.setEffectTime(effectTime);
        userDO.setExpireTime(expireTime);
        userDO.setToken(token);
    }

    @Override
    public Long addUser(UserVO userVO) {
        /** 参数校验 */
        checkUserParam(userVO);
        /** 已存在 */
        checkUserExist(userVO);
        UserDO userDO = CommonBeanConverter.doSingle(userVO, UserDO.class);
        /** 新增用户，会生成一个token，并且配置生效时间 */
        refreshToken(userDO);
        userMapper.add(userDO);
        return userDO.getId();
    }

    @Override
    public Boolean deleteUser(Long id) {
        UserDO userDO = userMapper.getById(id);
        if (ObjectUtils.isEmpty(userDO)) {
            return false;
        }
        return userMapper.delete(id) > 0;
    }

    @Override
    public Boolean updateUser(UserVO userVO) {
        UserDO userDO = userMapper.getById(userVO.getId());
        if (ObjectUtils.isEmpty(userDO)) {
            return false;
        }
        userDO = CommonBeanConverter.doSingle(userVO, UserDO.class);
        /** 更新用户，会重新生成一个token，并且配置生效时间 */
        refreshToken(userDO);
        userDO.setId(userVO.getId());
        return userMapper.update(userDO) > 0;
    }

    @Override
    public UserVO getById(Long id) {
        UserDO userDO = userMapper.getById(id);
        if (ObjectUtils.isEmpty(userDO)) {
            return null;
        }
        UserVO userVO = CommonBeanConverter.doSingle(userDO, UserVO.class);
        userVO.setId(id);
        return userVO;
    }

    @Override
    public String login(UserVO userVO) {
        /** 参数校验 */
        checkUserParam(userVO);
        /** 不存在 */
        UserDO userDO = userMapper.getByUsername(userVO.getUsername());
        if (ObjectUtils.isEmpty(userDO)) {
            throw new MysteriousException(ResponseCodeEnum.USER_NOT_EXIST);
        }
        /** 密码错误 */
        if (!userDO.getPassword().equals(userVO.getPassword())) {
            throw new MysteriousException(ResponseCodeEnum.USER_PASSWORD_ERROR);
        }
        /** 用户密码正确，允许登录，刷新token */
        refreshToken(userDO);
        userMapper.update(userDO);
        return userDO.getToken();
    }
}
