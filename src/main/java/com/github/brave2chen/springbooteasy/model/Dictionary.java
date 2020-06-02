package com.github.brave2chen.springbooteasy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.brave2chen.springbooteasy.validate.SaveGroup;
import com.github.brave2chen.springbooteasy.validate.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author brave2chen
 * @since 2020-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Dictionary对象", description = "数据字典")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    @Null(groups = {SaveGroup.class}, message = "ID，不需要传")
    @NotNull(groups = {UpdateGroup.class}, message = "ID，不能为空")
    @Min(value = 0, groups = {SaveGroup.class, UpdateGroup.class}, message = "ID，不能小于0")
    private Integer id;

    @ApiModelProperty(value = "父ID")
    @Min(value = 0, groups = {SaveGroup.class, UpdateGroup.class}, message = "父ID，不能小于0")
    private Integer parentId;

    @NotBlank(groups = {SaveGroup.class}, message = "字典类型，不能为空或空串")
    @Length(min = 1, max = 50, groups = {SaveGroup.class, UpdateGroup.class}, message = "字典类型，长度为 1-50 字符")
    @ApiModelProperty(value = "字典类型")
    private String type;

    @NotBlank(groups = {SaveGroup.class}, message = "显示名，不能为空或空串")
    @Length(min = 1, max = 100, groups = {SaveGroup.class, UpdateGroup.class}, message = "显示名，长度为 1-100 字符")
    @ApiModelProperty(value = "显示名")
    private String itemName;

    @NotBlank(groups = {SaveGroup.class}, message = "存储值，不能为空或空串")
    @Length(min = 1, max = 100, groups = {SaveGroup.class, UpdateGroup.class}, message = "存储值，长度为 1-100 字符")
    @ApiModelProperty(value = "存储值")
    private String itemValue;

    @NotBlank(groups = {SaveGroup.class}, message = "描述说明，不能为空或空串")
    @Length(min = 1, max = 100, groups = {SaveGroup.class, UpdateGroup.class}, message = "描述说明，长度为 1-100 字符")
    @ApiModelProperty(value = "描述说明")
    private String description;

    @NotNull(groups = {SaveGroup.class}, message = "序号，不能为空")
    @Min(value = 0, groups = {SaveGroup.class, UpdateGroup.class}, message = "序号，不能小于0")
    @ApiModelProperty(value = "序号")
    @TableField("`index`")
    private Integer index;

    @ApiModelProperty(value = "删除标记", hidden = true)
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

    @Null(groups = {SaveGroup.class, UpdateGroup.class}, message = "创建时间，不需要传")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;


}
