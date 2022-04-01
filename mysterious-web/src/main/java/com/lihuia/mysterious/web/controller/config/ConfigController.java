package com.lihuia.mysterious.web.controller.config;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.core.vo.config.ConfigVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午10:30
 */

@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private IConfigService configService;

    @PostMapping(value = "/add")
    public Response<Long> addConfig(@RequestBody ConfigVO configVO) {
        return ResponseUtil.buildSuccessResponse(configService.addConfig(configVO, UserUtils.getCurrent()));
    }

    @PostMapping(value = "/update")
    public Response<Boolean> updateConfig(@RequestBody ConfigVO configVO) {
        return ResponseUtil.buildSuccessResponse(configService.updateConfig(configVO, UserUtils.getCurrent()));
    }

    @GetMapping(value = "/delete")
    public Response<Boolean> deleteConfig(@RequestParam(value = "id") Long id) {
        return ResponseUtil.buildSuccessResponse(configService.deleteConfig(id));
    }

    @GetMapping(value = "/list")
    public Response<PageVO<ConfigVO>> getConfigList(@RequestParam(value = "key", required = false) String key,
                                                    @RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size) {
        return ResponseUtil.buildSuccessResponse(configService.getConfigList(key, page, size));
    }
}
