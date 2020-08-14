package com.github.brave2chen.springbooteasy.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author brave2chen
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 全量插入,等价于insert
     * {@link InsertBatchSomeColumn}
     *
     * @param entityList
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);

    /**
     * 全量更新，不忽略null字段，等价于update
     * 解决mybatis-plus会自动忽略null字段不更新
     * {@link com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById}
     *
     * @param entity
     * @return
     */
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

    /**
     * 根据 id 逻辑删除数据,并带字段填充功能
     * <p>注意入参是 entity !!! ,如果字段没有自动填充,就只是单纯的逻辑删除</p>
     * {@link LogicDeleteByIdWithFill}
     *
     * @param entity
     * @return
     */
    int deleteByIdWithFill(T entity);

}
