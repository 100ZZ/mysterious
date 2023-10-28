package com.lihuia.mysterious.service.service.user.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.user.UserMapper;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserParam;
import com.lihuia.mysterious.core.vo.user.UserQuery;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 10:15 PM
 */

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /** token有效时间12小时 */
    private final static int EXPIRE_TIME = 12;

    private void checkUserParam(UserParam userParam) {
        if (ObjectUtils.isEmpty(userParam)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isEmpty(userParam.getUsername())
                || StringUtils.isEmpty(userParam.getPassword())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkUserExist(UserParam userParam) {
        UserDO userDO = userMapper.getByUsername(userParam.getUsername());
        if (!ObjectUtils.isEmpty(userDO)) {
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
    public Long addUser(UserParam userParam) {
        /** 参数校验 */
        checkUserParam(userParam);
        /** 已存在 */
        checkUserExist(userParam);
        UserDO userDO = BeanConverter.doSingle(userParam, UserDO.class);
        /** 新增用户，会生成一个token，并且配置生效时间 */
        refreshToken(userDO);
        /** 用户密码通过BCrypt进行加密储存 */
        String slat = BCrypt.gensalt();
        userDO.setPassword(BCrypt.hashpw(userDO.getPassword(), slat));
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
    public Boolean updateUser(Long id, UserParam userParam) {
        UserDO userDO = userMapper.getById(id);
        if (ObjectUtils.isEmpty(userDO)) {
            return false;
        }
        userDO = BeanConverter.doSingle(userParam, UserDO.class);
        /** 更新用户，会重新生成一个token，并且配置生效时间 */
        refreshToken(userDO);
        userDO.setId(id);
        return userMapper.update(userDO) > 0;
    }

    @Override
    public UserVO getById(Long id) {
        UserDO userDO = userMapper.getById(id);
        if (ObjectUtils.isEmpty(userDO)) {
            return null;
        }
        //return UserVO.builder().id(id).username(userDO.getUsername()).password("******").build();
        return convert(userDO);
    }

    @Override
    public String login(UserParam userParam) {
        /** 参数校验 */
        checkUserParam(userParam);
        /** 不存在 */
        UserDO userDO = userMapper.getByUsername(userParam.getUsername());
        if (ObjectUtils.isEmpty(userDO)) {
            throw new MysteriousException(ResponseCodeEnum.USER_NOT_EXIST);
        }
        /** 密码错误 */
//        if (!userDO.getPassword().equals(userParam.getPassword())) {
//            throw new MysteriousException(ResponseCodeEnum.USER_PASSWORD_ERROR);
//        }
        /** 加密了，需要进行匹配验证，而不是简单是否相等 */
        if (!BCrypt.checkpw(userParam.getPassword(), userDO.getPassword())) {
            throw new MysteriousException(ResponseCodeEnum.USER_PASSWORD_ERROR);
        }
        /** 用户密码正确，允许登录，刷新token */
        refreshToken(userDO);
        userMapper.update(userDO);
        return userDO.getToken();
    }

    @Override
    public PageVO<UserVO> getUserList(UserQuery query) {
        PageVO<UserVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = userMapper.getUserCount(query.getUsername(), query.getRealName());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<UserDO> userList = userMapper.getUserList(query.getUsername(), query.getRealName(), offset, query.getSize());
            log.info("userList: {}", userList);
            pageVO.setList(userList.stream().map(this::convert).collect(Collectors.toList()));
        }
        return pageVO;
    }

    private UserVO convert(UserDO userDO) {
        //UserVO userVO = BeanConverter.doSingle(userDO, UserVO.class);
        UserVO userVO = new UserVO();
        userVO.setUsername(userDO.getUsername());
        userVO.setPassword(userDO.getPassword());
        userVO.setRealName(userDO.getRealName());
        userVO.setId(userDO.getId());
        userVO.setEffectTime(userDO.getEffectTime());
        userVO.setExpireTime(userDO.getExpireTime());
        return userVO;
//        return UserVO.builder().id(userDO.getId()).username(userDO.getUsername()).password("******").build();
    }
}
