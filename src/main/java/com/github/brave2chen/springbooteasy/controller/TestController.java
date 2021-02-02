package com.github.brave2chen.springbooteasy.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

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

    @Autowired
    private MeterRegistry meterRegistry;

    private Counter counter;

    @PostConstruct
    private void init() {
        counter = meterRegistry.counter("app_requests_method_count", "method", "hello");
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String hello() {
        try {
            counter.increment();
        } catch (Exception e) {
        }
        return "你有权限访问";
    }
}
