package com.lihuia.mysterious.core.entity.user;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lihuia.com
 * @date 2022/3/28 11:39 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO<Long> {

    /** 用户 */
    private String username;

    /** 密码 */
    private String password;

    /** token */
    private String token;

    /** 生效时间 */
    private LocalDateTime effectTime;

    /** 失效时间 */
    private LocalDateTime expireTime;
}
