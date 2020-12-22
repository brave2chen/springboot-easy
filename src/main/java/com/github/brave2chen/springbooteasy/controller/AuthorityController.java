package com.github.brave2chen.springbooteasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Pagination;
import com.github.brave2chen.springbooteasy.config.security.MySecurityMetadataSource;
import com.github.brave2chen.springbooteasy.core.BaseController;
import com.github.brave2chen.springbooteasy.entity.Authority;
import com.github.brave2chen.springbooteasy.query.AuthorityQuery;
import com.github.brave2chen.springbooteasy.service.AuthorityService;
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
 * 权限 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-09-15
 */
@Api(tags = "权限 CURD服务")
@RestController
@Validated
@RequestMapping(value = "/authority", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorityController extends BaseController {
    @Autowired
    private AuthorityService service;

    @Autowired
    private MySecurityMetadataSource securityMetadataSource;

    @ApiOperation("分页查询 权限 列表")
    @GetMapping("")
    public JsonResult page(@Valid AuthorityQuery entity, Pagination pagination) throws Exception {
        QueryWrapper<Authority> queryWrapper = super.buildQueryWrapper(entity);

        List<Authority> list = service.getEntityList(queryWrapper, pagination);

        return JsonResult.OK(list).bindPagination(pagination);
    }

    @ApiOperation("获取 权限 详细信息")
    @GetMapping("/{id:\\d+}")
    public Authority get(@PathVariable Long id) throws Exception {
        return service.getById(id);
    }

    @ApiOperation("创建权限")
    @PostMapping("")
    public boolean save(@Valid @RequestBody Authority entity) throws Exception {
        return service.save(entity);
    }

    @ApiOperation("更新权限")
    @PutMapping("/{id:\\d+}")
    public boolean update(@PathVariable Long id, @Valid @RequestBody Authority entity) throws Exception {
        return service.updateById(entity.setId(id));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{id:\\d+}")
    public boolean delete(@PathVariable Long id) throws Exception {
        return service.removeById(id);
    }

    @ApiOperation("刷新权限")
    @PostMapping("/refresh")
    public boolean refresh() throws Exception {
        securityMetadataSource.refresh();
        return true;
    }
}
