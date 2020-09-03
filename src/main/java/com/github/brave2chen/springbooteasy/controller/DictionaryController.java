package com.github.brave2chen.springbooteasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diboot.core.controller.BaseController;
import com.diboot.core.entity.Dictionary;
import com.diboot.core.service.DictionaryService;
import com.diboot.core.util.V;
import com.diboot.core.vo.DictionaryVO;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.KeyValue;
import com.diboot.core.vo.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 数据字典相关Controller
 *
 * @author www.dibo.ltd
 * @version 1.0
 * @date 2020-06-27
 * Copyright © dibo.ltd
 */
@RestController
@Slf4j
public class DictionaryController extends BaseController {
    @Resource
    private DictionaryService dictionaryService;

    /***
     * 查询ViewObject的分页数据
     * <p>
     * url请求参数示例: /list?name=abc&pageSize=20&pageIndex=1&orderBy=name
     * </p>
     * @return
     * @throws Exception
     */
    @GetMapping("/dictionary/list")
    public JsonResult getViewObjectListMapping(Dictionary entity, Pagination pagination) throws Exception {
        QueryWrapper<Dictionary> queryWrapper = super.buildQueryWrapper(entity);
        List<DictionaryVO> voList = dictionaryService.getViewObjectList(queryWrapper, pagination, DictionaryVO.class);
        return JsonResult.OK(voList).bindPagination(pagination);
    }

    /***
     * 根据资源id查询ViewObject
     * @param id ID
     * @return
     * @throws Exception
     */
    @GetMapping("/dictionary/{id}")
    public DictionaryVO getViewObjectMapping(@PathVariable("id") Long id) throws Exception {
        return dictionaryService.getViewObject(id, DictionaryVO.class);
    }

    /**
     * 创建资源对象
     *
     * @param entityVO
     * @return JsonResult
     * @throws Exception
     */
    @PostMapping("/dictionary")
    public Boolean createEntityMapping(@RequestBody @Valid DictionaryVO entityVO) throws Exception {
        boolean success = dictionaryService.createDictAndChildren(entityVO);
        return success;
    }

    /***
     * 根据ID更新资源对象
     * @param entityVO
     * @return JsonResult
     * @throws Exception
     */
    @PutMapping("/dictionary/{id}")
    public Boolean updateEntityMapping(@PathVariable("id") Long id, @Valid @RequestBody DictionaryVO entityVO) throws Exception {
        entityVO.setId(id);
        boolean success = dictionaryService.updateDictAndChildren(entityVO);
        return success;
    }

    /***
     * 根据id删除资源对象
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/dictionary/{id}")
    public Boolean deleteEntityMapping(@PathVariable("id") Long id) throws Exception {
        boolean success = dictionaryService.deleteDictAndChildren(id);
        return success;
    }

    /***
     * 获取数据字典数据列表
     * @param type
     * @return
     * @throws Exception
     */
    @GetMapping("/dictionary/items/{type}")
    public List<KeyValue> getItems(@PathVariable("type") @NotBlank(message = "type参数未指定") String type) throws Exception {
        List<KeyValue> itemsList = dictionaryService.getKeyValueList(type);
        return itemsList;
    }

    /**
     * 校验类型编码是否重复
     *
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/dictionary/checkTypeDuplicate")
    public boolean checkTypeDuplicate(@RequestParam(required = false) Long id, @NotBlank(message = "type参数未指定") String type) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper();
        wrapper.select(Dictionary::getId).eq(Dictionary::getType, type).eq(Dictionary::getParentId, 0);
        if (V.notEmpty(id)) {
            wrapper.ne(Dictionary::getId, id);
        }
        boolean alreadyExists = dictionaryService.exists(wrapper);
        return alreadyExists;
    }
}
