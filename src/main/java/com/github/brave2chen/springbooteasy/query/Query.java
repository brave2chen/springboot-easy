package com.github.brave2chen.springbooteasy.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Query
 *
 * @author chenqy28
 * @date 2020-07-04
 */
abstract class Query<T> {
    public LambdaQueryWrapper<T> wrapper() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
            T model = clazz.newInstance();
            BeanUtils.copyProperties(this, model);
            return Wrappers.lambdaQuery(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
