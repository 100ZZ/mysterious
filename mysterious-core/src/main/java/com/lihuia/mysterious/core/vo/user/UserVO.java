package com.lihuia.mysterious.core.vo.user;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:38 PM
 */

@Data
public class UserVO extends BaseVO<Long> {

    /** 用户 */
    private String username;

    /** 密码 */
    private String password;
}
