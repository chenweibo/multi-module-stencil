package com.chen.stencil.config;

import com.chen.stencil.common.config.BaseSwaggerConfig;
import com.chen.stencil.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.chen.stencil.controller")
                .title("后台系统文档")
                .description("后台模块文档")
                .contactName("chen")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}

