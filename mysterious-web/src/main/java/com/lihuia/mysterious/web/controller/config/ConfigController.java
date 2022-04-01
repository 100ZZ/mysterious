package com.lihuia.mysterious.web.controller.config;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.config.ConfigQuery;
import com.lihuia.mysterious.core.vo.config.ConfigVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.web.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午10:30
 */

@RestController
@Api(tags = "配置管理")
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private IConfigService configService;

    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public Response<Long> addConfig(@RequestBody ConfigVO configVO) {
        return ResponseUtil.buildSuccessResponse(configService.addConfig(configVO, UserUtils.getCurrent()));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/update")
    public Response<Boolean> updateConfig(@RequestBody ConfigVO configVO) {
        return ResponseUtil.buildSuccessResponse(configService.updateConfig(configVO, UserUtils.getCurrent()));
    }

    @ApiOperation("删除")
    @GetMapping(value = "/delete")
    public Response<Boolean> deleteConfig(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(configService.deleteConfig(id));
    }

    @ApiOperation("分页查询")
    @GetMapping(value = "/list")
    public Response<PageVO<ConfigVO>> getConfigList(ConfigQuery configQuery) {
        return ResponseUtil.buildSuccessResponse(configService.getConfigList(configQuery));
    }
}
