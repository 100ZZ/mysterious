package com.lihuia.mysterious.core.domain.user;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:38 PM
 */

public class UserVO {

    /** 用户 */
    private String username;

    /** 密码 */
    private String password;

    /** token */
    private String token;

    /** 过期时间 */
    private LocalDateTime expireTime;

    /** 最后一次登录时间 */
    private LocalDateTime lastLoginTime;
}
