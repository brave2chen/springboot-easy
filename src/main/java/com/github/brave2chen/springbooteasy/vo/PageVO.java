package com.github.brave2chen.springbooteasy.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分页查询结果包装类
 *
 * @author chenqy28
 * @date 2020-06-02
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分页查询结果包装类")
public class PageVO<T> {
    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private int size;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private int page;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "记录总数")
    private int total;

    /**
     * 当前页记录
     */
    @ApiModelProperty(value = "当前页记录")
    private List data;

    public static <T> PageVO<T> of(Page<T> page) {
        return new PageVO<T>()
                .setPage((int) page.getCurrent())
                .setSize((int) page.getSize())
                .setTotal((int) page.getTotal())
                .setData(page.getRecords());
    }
}
