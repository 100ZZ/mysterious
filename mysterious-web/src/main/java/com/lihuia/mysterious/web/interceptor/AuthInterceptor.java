package com.lihuia.mysterious.web.interceptor;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.user.UserMapper;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.web.utils.TokenUtils;
import com.lihuia.mysterious.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2022/3/28 11:22 PM
 */

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    /** 用户请求传的是token，而不是用户密码，因此可以拦截进行权限校验 */
    //todo: 目前是拦截抛出异常，理论上应该是和前端对接跳转
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = TokenUtils.getToken(request);
        if (StringUtils.isEmpty(token)) {
            log.warn(ResponseCodeEnum.USER_NOT_LOGIN.getMessage());
            throw new MysteriousException(ResponseCodeEnum.USER_NOT_LOGIN);
        }
        UserDO userDO = userMapper.getByToken(token);
        if (ObjectUtils.isEmpty(userDO)) {
            log.warn(ResponseCodeEnum.USER_NOT_EXIST.getMessage());
            throw new MysteriousException(ResponseCodeEnum.USER_NOT_EXIST);
        }
        if (userDO.getExpireTime().isBefore(LocalDateTime.now())) {
            log.warn(ResponseCodeEnum.USER_TOKEN_EXPIRE.getMessage());
            throw new MysteriousException(ResponseCodeEnum.USER_TOKEN_EXPIRE);
        }
        /** token有效，ThreadLocal获取用户信息，接口调用传递用户信息 */
        UserUtils.setCurrent(UserVO.builder().id(userDO.getId())
                .username(userDO.getUsername()).password(userDO.getPassword()).build());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserUtils.removeCurrent();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
