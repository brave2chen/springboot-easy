package com.github.brave2chen.springbooteasy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置类
 *
 * @author brave2chen
 * @date 2020-05-29
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String BASE_PACKAGE = "com.github.brave2chen.springbooteasy.rest";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("资源管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot 脚手架 APIs")
                .description("Spring Boot 脚手架 APIs")
                .termsOfServiceUrl("https://github.com/brave2chen/springboot-easy")
                .contact(new Contact("brave2chen", "https://github.com/brave2chen/springboot-easy", "bravechen@foxmail.com"))
                .version("1.0")
                .build();
    }
}