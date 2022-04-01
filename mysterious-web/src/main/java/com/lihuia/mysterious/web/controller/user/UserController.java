package com.lihuia.mysterious.web.controller.user;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.page.PageVO;
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
    public Response<Long> addUser(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.addUser(userVO));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete")
    public Response<Boolean> deleteUser(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(userService.deleteUser(id));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/update")
    public Response<Boolean> updateUser(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.updateUser(userVO));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/getById")
    public Response<UserVO> getById(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(userService.getById(id));
    }

    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public Response<String> login(@RequestBody UserVO userVO) {
        return ResponseUtil.buildSuccessResponse(userService.login(userVO));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<UserVO>> getUserList(UserQuery userQuery) {
        return ResponseUtil.buildSuccessResponse(userService.getUserList(userQuery));
    }
}
