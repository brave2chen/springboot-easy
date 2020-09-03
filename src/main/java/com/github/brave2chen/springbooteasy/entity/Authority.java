package com.github.brave2chen.springbooteasy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.brave2chen.springbooteasy.core.LogicalEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Authority extends LogicalEntity {

    private static final long serialVersionUID = -7171698062371683745L;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "接口方法")
    private String method;

    @ApiModelProperty(value = "接口路径")
    private String path;

    @ApiModelProperty(value = "权限KEY", hidden = true)
    @JsonIgnore
    public String getAuthority() {
        return Optional.ofNullable(this.getMethod()).orElse("") + this.getPath();
    }
}
