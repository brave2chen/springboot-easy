package com.github.brave2chen.springbooteasy.rest;


import com.github.brave2chen.springbooteasy.model.Dictionary;
import com.github.brave2chen.springbooteasy.query.DictionaryQuery;
import com.github.brave2chen.springbooteasy.service.DictionaryService;
import com.github.brave2chen.springbooteasy.validate.SaveGroup;
import com.github.brave2chen.springbooteasy.validate.UpdateGroup;
import com.github.brave2chen.springbooteasy.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author brave2chen
 * @since 2020-06-01
 */
@Api(tags = "数据字典 CURD服务")
@RestController
@RequestMapping(value = "/dictionary", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation("分页查询 数据字典 列表")
    @GetMapping("")
    public PageVO page(@Valid DictionaryQuery query) {
        return PageVO.of(dictionaryService.page(query.page(), query.wrapper().lambda().orderByAsc(Dictionary::getType, Dictionary::getIndex)));
    }

    @ApiOperation("查询 数据字典 信息")
    @GetMapping("/{id:\\d+}")
    public Dictionary get(@PathVariable int id) {
        return dictionaryService.getById(id);
    }

    @ApiOperation(value = "新增 数据字典 信息", notes = "新增 数据字典 信息")
    @PostMapping(value = "")
    public Dictionary save(@RequestBody @Validated(SaveGroup.class) @Valid Dictionary model) {
        dictionaryService.save(model);
        return model;
    }

    @ApiOperation("更新 数据字典 信息")
    @PutMapping("/{id:\\d+}")
    public boolean updateAll(@PathVariable int id, @RequestBody @Validated(SaveGroup.class) @Valid Dictionary model) {
        return dictionaryService.updateById(model.setId(id));
    }

    @ApiOperation("部分更新 数据字典 信息")
    @PatchMapping("/{id:\\d+}")
    public boolean update(@PathVariable int id, @RequestBody @Validated(UpdateGroup.class) @Valid Dictionary model) {
        return dictionaryService.updateById(model.setId(id));
    }

    @ApiOperation("删除 数据字典 信息")
    @DeleteMapping("/{id:\\d+}")
    public boolean delete(@PathVariable int id) {
        return dictionaryService.removeById(id);
    }
}

