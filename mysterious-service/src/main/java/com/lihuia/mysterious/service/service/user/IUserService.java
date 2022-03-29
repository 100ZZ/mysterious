package com.lihuia.mysterious.service.service.user;

import com.lihuia.mysterious.core.domain.user.UserVO;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:15 PM
 */

public interface IUserService {

    /**
     * 新增用户
     * @param userVO
     * @return
     */
    Long addUser(UserVO userVO);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Integer deleteUser(Long id);

    /**
     * 更新用户
     * @param userVO
     * @return
     */
    Integer updateUser(UserVO userVO);

    /**
     * 查询用户
     * @param id
     * @return
     */
    UserVO getById(Long id);
}
