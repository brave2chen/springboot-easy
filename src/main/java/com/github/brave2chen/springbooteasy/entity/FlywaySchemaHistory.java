package com.github.brave2chen.springbooteasy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * FlywaySchemaHistory对象
 * </p>
 *
 * @author brave2chen
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FlywaySchemaHistory对象", description = "")
public class FlywaySchemaHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "installed_rank", type = IdType.AUTO)
    private Integer installedRank;

    private String version;

    private String description;

    private String type;

    private String script;

    private Integer checksum;

    private String installedBy;

    private Date installedOn;

    private Integer executionTime;

    private Boolean success;
}
