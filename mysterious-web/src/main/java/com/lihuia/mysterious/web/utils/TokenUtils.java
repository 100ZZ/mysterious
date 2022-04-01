package com.lihuia.mysterious.web.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lihuia.com
 * @date 2022/3/30 11:17 PM
 */

public class TokenUtils {

    /**
     * 从请求里捞token字段；先查看header，如果没有的话，再查看参数
     * @param httpRequest
     * @return
     */
    public static String getToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;
    }
}
