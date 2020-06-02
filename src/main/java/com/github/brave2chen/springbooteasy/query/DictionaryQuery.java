package com.github.brave2chen.springbooteasy.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.brave2chen.springbooteasy.model.Dictionary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;

/**
 * 数据字典 查询实体
 *
 * @author chenqy28
 * @date 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "数据字典 查询实体", description = "数据字典 查询实体")
public class DictionaryQuery extends PageQuery<Dictionary> {
    @ApiModelProperty(value = "父ID")
    @Min(value = 0, message = "父ID，不能小于0")
    private Integer parentId;

    @ApiModelProperty(value = "字典类型")
    @Length(min = 1, max = 50, message = "字典类型，长度为 1-50 字符")
    private String type;

    @Length(min = 1, max = 100, message = "存储值，长度为 1-100 字符")
    @ApiModelProperty(value = "存储值")
    private String itemValue;

    public QueryWrapper<Dictionary> wrapper() {
        Dictionary model = new Dictionary();
        BeanUtils.copyProperties(this, model);
        return Wrappers.query(model);
    }
}
