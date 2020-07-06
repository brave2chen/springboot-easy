package com.github.brave2chen.springbooteasy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Authority对象", description = "权限")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "接口方法")
    private String method;

    @ApiModelProperty(value = "接口路径")
    private String path;

    @ApiModelProperty(value = "删除标记", hidden = true)
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

    @ApiModelProperty(value = "创建用户")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "权限KEY", hidden = true)
    @JsonIgnore
    public String getAuthority() {
        return Optional.ofNullable(this.getMethod()).orElse("") + this.getPath();
    }
}
