package com.lihuia.mysterious.web.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/17 10:51 AM
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenParameter = new ParameterBuilder();
        List<Parameter> parameterList = new ArrayList<>();
        tokenParameter
                .name("token")
                .description("用户token认证")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        parameterList.add(tokenParameter.build());

        return new Docket(DocumentationType.SWAGGER_2)
                // 指定构建api文档的详细信息的方法：apiInfo()
                .apiInfo(apiInfo())
                .select()
                // 指定要生成api接口的包路径
                .apis(RequestHandlerSelectors.basePackage("com.lihuia.mysterious.web.controller"))
                //使用了 @ApiOperation 注解的方法生成api接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                //可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build().globalOperationParameters(parameterList).apiInfo(apiInfo());
    }

    /**
     * 设置api文档的详细信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("Mysterious压测平台后端服务集成Swagger")
                // 接口描述
                .description("Rest接口")
                // 联系方式
                .contact(new Contact("lihui", "https://lihuia.com", "maple@lihuia.com"))
                // 版本信息
                .version("1.0")
                // 构建
                .build();
    }
}
