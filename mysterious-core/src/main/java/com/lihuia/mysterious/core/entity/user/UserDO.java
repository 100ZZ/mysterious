package com.lihuia.mysterious.core.entity.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2022/3/28 11:39 PM
 */

@Data
public class UserDO {

    /** 主键 */
    private Long id;

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
