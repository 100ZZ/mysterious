package com.lihuia.mysterious.service.service.user;

import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;

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
    Boolean deleteUser(Long id);

    /**
     * 更新用户
     * @param userVO
     * @return
     */
    Boolean updateUser(UserVO userVO);

    /**
     * 查询用户
     * @param id
     * @return
     */
    UserVO getById(Long id);

    /**
     * 用户登录
     * @param userVO
     * @return
     */
    String login(UserVO userVO);

    /**
     * 分页查询
     * @param username
     * @param page
     * @param size
     * @return
     */
    PageVO<UserVO> getUserList(String username, Integer page, Integer size);
}
