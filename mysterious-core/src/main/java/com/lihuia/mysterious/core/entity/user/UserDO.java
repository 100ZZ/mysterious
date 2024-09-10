package com.lihuia.mysterious.core.entity.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2023/3/28 11:39 PM
 */

@Data
public class UserDO {

    private Long id;

    /** 用户 */
    private String username;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String realName;

    /** token */
    private String token;

    /** 生效时间 */
    private LocalDateTime effectTime;

    /** 失效时间 */
    private LocalDateTime expireTime;
}
