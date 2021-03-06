package com.lihuia.mysterious;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lihuia.com
 * @date 2022/3/27 1:07 PM
 */

@MapperScan(basePackages = "com.lihuia.mysterious.core.mapper")
@SpringBootApplication
public class MysteriousApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MysteriousApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------Mysterious-----------");
    }
}