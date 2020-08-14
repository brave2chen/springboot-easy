package com.github.brave2chen.springbooteasy.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * DibootCore自动配置类
 * 字典表改为使用flyway生成
 *
 * @author mazc@dibo.ltd
 * @version v2.0
 * @date 2019/08/01
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"com.diboot.core"})
@MapperScan(basePackages = {"com.diboot.core.mapper"})
@Order(1)
public class DiBootConfig implements WebMvcConfigurer {

}
