package com.lihuia.mysterious.web.controller.unittest;

import com.lihuia.mysterious.common.response.Response;
import com.lihuia.mysterious.common.response.ResponseUtil;
import com.lihuia.mysterious.service.service.unittest.IHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maple@lihuia.com
 * @date 2023/4/23 8:28 PM
 */

@RestController
@RequestMapping(value = "/unittest")
public class UnittestController {

    @Autowired
    private IHelloService helloService;

    @GetMapping(value = "/hello")
    public Response<String> sayHello(@RequestParam(value = "message") String message) {
        return ResponseUtil.buildSuccessResponse(helloService.sayHello(message));
    }
}
