package com.github.brave2chen.springbooteasy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diboot.core.controller.BaseController;
import com.diboot.core.util.BeanUtils;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Pagination;
import com.github.brave2chen.springbooteasy.config.security.SecurityUser;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.query.UserQuery;
import com.github.brave2chen.springbooteasy.service.UserService;
import com.github.brave2chen.springbooteasy.vo.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public UserInfo info() throws Exception {
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserInfo) UserInfo.from(user.getUser()).setPassword("******");
    }

    @ApiOperation("分页查询 用户 列表")
    @GetMapping("")
    public JsonResult page(@Valid UserQuery user, Pagination pagination) throws Exception {
        QueryWrapper<User> queryWrapper = super.buildQueryWrapper(user);
        if (StringUtils.isNotBlank(user.getIdentity())) {
            queryWrapper.and(w -> w
                    .or().like(BeanUtils.convertToFieldName(User::getUsername), user.getIdentity())
                    .or().like(BeanUtils.convertToFieldName(User::getEmail), user.getIdentity())
                    .or().like(BeanUtils.convertToFieldName(User::getMobile), user.getIdentity())
                    .or().like(BeanUtils.convertToFieldName(User::getNickname), user.getIdentity())
            );
        }
        List<UserInfo> users = userService.getViewObjectList(queryWrapper, pagination, UserInfo.class);
        users.forEach(u -> u.setPassword("*****"));
        return JsonResult.OK(users).bindPagination(pagination);
    }


    @ApiOperation("创建用户")
    @PostMapping("")
    public boolean save(@Valid @RequestBody User user) throws Exception {
        return userService.save(user);
    }

    @ApiOperation("更新用户")
    @PutMapping("/{id:\\d+}")
    public boolean update(@PathVariable Long id, @Valid @RequestBody User user) throws Exception {
        user.setId(id);
        return userService.updateById(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id:\\d+}")
    public boolean update(@PathVariable Long id) throws Exception {
        return userService.removeById(id);
    }
}

