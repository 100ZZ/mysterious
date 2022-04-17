package com.lihuia.mysterious.web.controller.user;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserParam;
import com.lihuia.mysterious.core.vo.user.UserQuery;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.user.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:43 PM
 */

@RestController
@Api(tags = "用户权限管理")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public Response<Long> addUser(@RequestBody UserParam userParam) {
        return ResponseUtil.buildSuccessResponse(userService.addUser(userParam));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Response<Boolean> deleteUser(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(userService.deleteUser(id));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/update/{id}")
    public Response<Boolean> updateUser(@PathVariable Long id,
                                        @RequestBody UserParam userParam) {
        return ResponseUtil.buildSuccessResponse(userService.updateUser(id, userParam));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/getById/{id}")
    public Response<UserVO> getById(@PathVariable Long id) {
        return ResponseUtil.buildSuccessResponse(userService.getById(id));
    }

    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public Response<String> login(@RequestBody UserParam userParam) {
        return ResponseUtil.buildSuccessResponse(userService.login(userParam));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<UserVO>> getUserList(UserQuery userQuery) {
        return ResponseUtil.buildSuccessResponse(userService.getUserList(userQuery));
    }
}
