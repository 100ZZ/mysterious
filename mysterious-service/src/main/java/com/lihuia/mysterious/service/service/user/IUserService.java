package com.lihuia.mysterious.service.service.user;

import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserParam;
import com.lihuia.mysterious.core.vo.user.UserQuery;
import com.lihuia.mysterious.core.vo.user.UserVO;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:15 PM
 */

public interface IUserService {

    /**
     * 新增用户
     * @param userParam
     * @return
     */
    Long addUser(UserParam userParam);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Boolean deleteUser(Long id);

    /**
     * 更新用户
     * @param id
     * @param userParam
     * @return
     */
    Boolean updateUser(Long id, UserParam userParam);

    /**
     * 查询用户
     * @param id
     * @return
     */
    UserVO getById(Long id);

    /**
     * 用户登录
     * @param userParam
     * @return
     */
    String login(UserParam userParam);

    /**
     * 分页查询
     * @param query
     * @return
     */
    PageVO<UserVO> getUserList(UserQuery query);
}
