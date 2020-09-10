package com.github.brave2chen.springbooteasy.query;

import com.diboot.core.binding.query.BindQuery;
import com.diboot.core.binding.query.Comparison;
import com.github.brave2chen.springbooteasy.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * User查询对象
 *
 * @author brave2chen
 * @date 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "数据字典 查询实体", description = "数据字典 查询实体")
public class UserQuery extends User {
    @BindQuery(comparison = Comparison.IN, field = "gender")
    private Integer[] sex;

    /** 需要自行实现 */
    @BindQuery(ignore = true)
    private String identity;
}
