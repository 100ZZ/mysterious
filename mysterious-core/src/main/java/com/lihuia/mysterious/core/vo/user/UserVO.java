package com.lihuia.mysterious.core.vo.user;

import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:38 PM
 */

@Data
public class UserVO {

    /** 主键 */
    private Long id;

    /** 用户 */
    private String username;

    /** 密码 */
    private String password;
}
