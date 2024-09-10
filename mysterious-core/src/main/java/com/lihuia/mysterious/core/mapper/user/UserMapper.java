package com.lihuia.mysterious.core.mapper.user;

import com.lihuia.mysterious.core.entity.user.UserDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/3/29 10:08 PM
 */

public interface UserMapper extends BaseMapper<UserDO> {


    /**
     * 根据username查询用户是否已存在
     * @param username
     * @return
     */
    UserDO getByUsername(String username);

    /**
     * 根据token查询用户是否存在
     * @param token
     * @return
     */
    UserDO getByToken(String token);

    /**
     * 分页查询total
     * @param username
     * @param realName
     * @return
     */
    Integer getUserCount(String username, String realName);

    /**
     * 分页查询list
     * @param username
     * @param realName
     * @param offset
     * @param limit
     * @return
     */
    List<UserDO> getUserList(String username, String realName, Integer offset, Integer limit);
}
