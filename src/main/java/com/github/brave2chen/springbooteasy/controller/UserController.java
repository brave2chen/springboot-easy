package com.github.brave2chen.springbooteasy.controller;


import com.github.brave2chen.springbooteasy.query.UserQuery;
import com.github.brave2chen.springbooteasy.service.UserService;
import com.github.brave2chen.springbooteasy.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Api(tags = "用户 CURD服务")
@RestController
@Validated
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("分页查询 用户 列表")
    @GetMapping("")
    public PageVO page(@Valid UserQuery query) {
        return PageVO.of(userService.page(query));
    }
}

