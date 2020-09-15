package com.github.brave2chen.springbooteasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RestController;
import com.github.brave2chen.springbooteasy.core.BaseController;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Pagination;
import com.github.brave2chen.springbooteasy.entity.FlywaySchemaHistory;
import com.github.brave2chen.springbooteasy.service.FlywaySchemaHistoryService;
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
 * Flyway版本 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-09-15
 */
@Api(tags = " CURD服务")
@RestController
@Validated
@RequestMapping(value = "/flywaySchemaHistory", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlywaySchemaHistoryController extends BaseController {
    @Autowired
    private FlywaySchemaHistoryService service;

    @ApiOperation("分页查询 Flyway版本 列表")
    @GetMapping("")
    public JsonResult page(@Valid FlywaySchemaHistory entity, Pagination pagination) throws Exception {
        QueryWrapper<FlywaySchemaHistory> queryWrapper = super.buildQueryWrapper(entity);

        List<FlywaySchemaHistory> list = service.getEntityList(queryWrapper, pagination);

        return JsonResult.OK(list).bindPagination(pagination);
    }

    @ApiOperation("获取 Flyway版本 详细信息")
    @GetMapping("/{id:\\d+}")
    public FlywaySchemaHistory get(@PathVariable Long id) throws Exception {
        return service.getById(id);
    }
}
