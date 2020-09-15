package com.github.brave2chen.springbooteasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RestController;
import com.github.brave2chen.springbooteasy.core.BaseController;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Pagination;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-09-15
 */
@Api(tags = "角色 CURD服务")
@RestController
@Validated
@RequestMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController extends BaseController {
    @Autowired
    private RoleService service;

    @ApiOperation("分页查询 角色 列表")
    @GetMapping("")
    public JsonResult page(@Valid Role entity, Pagination pagination) throws Exception {
        QueryWrapper<Role> queryWrapper = super.buildQueryWrapper(entity);

        List<Role> list = service.getEntityList(queryWrapper, pagination);

        return JsonResult.OK(list).bindPagination(pagination);
    }

    @ApiOperation("获取 角色 详细信息")
    @GetMapping("/{id:\\d+}")
    public Role get(@PathVariable Long id) throws Exception {
        return service.getById(id);
    }

    @ApiOperation("创建角色")
    @PostMapping("")
    public boolean save(@Valid @RequestBody Role entity) throws Exception {
        return service.save(entity);
    }

    @ApiOperation("更新角色")
    @PutMapping("/{id:\\d+}")
    public boolean update(@PathVariable Long id, @Valid @RequestBody Role entity) throws Exception {
        entity.setId(id);
        return service.updateById(entity);
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id:\\d+}")
    public boolean update(@PathVariable Long id) throws Exception {
        return service.removeById(id);
    }
}
