package com.github.brave2chen.springbooteasy.controller;


import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Pagination;
import com.github.brave2chen.springbooteasy.entity.FlywaySchemaHistory;
import com.github.brave2chen.springbooteasy.service.FlywaySchemaHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-05-29
 */
@Api(tags = "Flyway 版本查询服务")
@RestController
@RequestMapping(value = "/flywaySchemaHistory", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlywaySchemaHistoryController {

    @Autowired
    private FlywaySchemaHistoryService flywaySchemaHistoryService;

    @ApiOperation("分页查询 Flyway 版本列表")
    @GetMapping("")
    public JsonResult page(Pagination pagination) {
        return JsonResult.OK(flywaySchemaHistoryService.page(pagination.toPage()).getRecords()).bindPagination(pagination);
    }

    @ApiOperation(value = "查询 Flyway 版本信息", notes = "通过id查询，id 不小于0")
    @GetMapping(value = "/{id:\\d+}")
    public FlywaySchemaHistory get(@PathVariable int id) {
        return flywaySchemaHistoryService.getById(id);
    }
}

