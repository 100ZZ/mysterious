package com.lihuia.mysterious.web.interceptor;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.user.UserMapper;
import com.lihuia.mysterious.web.request.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            log.error(ResponseCodeEnum.USER_NOT_LOGIN.getMessage());
            return false;
        }
        UserDO userDO = userMapper.getByToken(token);
        if (ObjectUtils.isEmpty(userDO)) {
            log.error(ResponseCodeEnum.USER_NOT_EXIST.getMessage());
            return false;
        }
        if (userDO.getExpireTime().isBefore(LocalDateTime.now())) {
            log.error(ResponseCodeEnum.USER_TOKEN_EXPIRE.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
