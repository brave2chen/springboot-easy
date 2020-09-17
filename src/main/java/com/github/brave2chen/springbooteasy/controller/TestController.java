package com.github.brave2chen.springbooteasy.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author brave2chen
 * @date 2020-09-17
 */
@Api(tags = "测试 服务")
@RestController
@Validated
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String hello() {
        return "你有权限访问";
    }
}
