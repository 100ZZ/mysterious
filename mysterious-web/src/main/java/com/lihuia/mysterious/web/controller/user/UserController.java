package com.lihuia.mysterious.web.controller.user;

import com.lihuia.mysterious.common.response.ApiResponse;
import com.lihuia.mysterious.core.domain.user.UserVO;
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
    public ApiResponse<Long> addUser(@RequestBody UserVO userVO) {
        return ApiResponse.returnSuccess(userService.addUser(userVO));
    }

    @GetMapping(value = "/delete")
    public ApiResponse<Integer> deleteUser(@RequestParam(value = "id") Long id) {
        return ApiResponse.returnSuccess(userService.deleteUser(id));
    }

    @PostMapping(value = "/update")
    public ApiResponse<Integer> updateUser(@RequestBody UserVO userVO) {
        return ApiResponse.returnSuccess(userService.updateUser(userVO));
    }

    @GetMapping(value = "/getById")
    public ApiResponse<UserVO> getById(@RequestParam(value = "id") Long id) {
        return ApiResponse.returnSuccess(userService.getById(id));
    }
}
