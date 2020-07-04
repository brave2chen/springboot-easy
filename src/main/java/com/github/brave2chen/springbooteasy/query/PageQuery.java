package com.github.brave2chen.springbooteasy.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.brave2chen.springbooteasy.config.MybatisPlusConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页查询条件实体基类
 *
 * @author brave2chen
 * @date 2020-06-02
 */
@Getter
@Accessors(chain = true)
public class PageQuery<T> extends Query<T> {
    /**
     * 每页条数，默认 10
     */
    @ApiModelProperty(value = "每页条数，默认 10")
    @Min(value = 1, message = "每页条数不应小于1")
    @Max(value = MybatisPlusConfig.PAGE_SIZE_LIMIT, message = "每页条数最大支持 " + MybatisPlusConfig.PAGE_SIZE_LIMIT)
    private int size = 10;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页，默认 1")
    @Min(value = 1, message = "当前页不应小于1")
    private int page = 1;

    public T setPage(int page) {
        this.page = page;
        return (T) this;
    }

    public T setSize(int size) {
        this.size = size;
        return (T) this;
    }

    public Page<T> page() {
        return new Page<T>(this.page, this.size);
    }
}
