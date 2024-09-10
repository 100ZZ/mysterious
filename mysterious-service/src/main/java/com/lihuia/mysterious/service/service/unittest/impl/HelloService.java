package com.lihuia.mysterious.service.service.unittest.impl;

import com.lihuia.mysterious.service.service.unittest.IHelloService;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2023/4/23 8:27 PM
 */

@Service
public class HelloService implements IHelloService {

    @Override
    public String sayHello(String message) {
        return "hello " + message;
    }
}
