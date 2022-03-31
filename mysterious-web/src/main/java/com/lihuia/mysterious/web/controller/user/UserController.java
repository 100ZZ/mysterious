package com.lihuia.mysterious.web.controller.user;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:43 PM
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/add")
    public Response<Long> addUser(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.addUser(userVO));
    }

    @GetMapping(value = "/delete")
    public Response<Boolean> deleteUser(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(userService.deleteUser(id));
    }

    @PostMapping(value = "/update")
    public Response<Boolean> updateUser(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.updateUser(userVO));
    }

    @GetMapping(value = "/getById")
    public Response<UserVO> getById(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(userService.getById(id));
    }

    @PostMapping(value = "/login")
    public Response<String> login(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.login(userVO));
    }

    @GetMapping(value = "/list")
    public Response<PageVO<UserVO>> getUserList(@RequestParam(value = "username", required = false) String username,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "size") Integer size) {
        return ResponseUtil.buildSuccessResponse(userService.getUserList(username, page, size));
    }
}
