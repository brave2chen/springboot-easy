/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.brave2chen.springbooteasy.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * LogicalEntity
 *
 * @author brave2chen
 */
@Getter
public abstract class LogicalDataEntity extends DataEntity {
    @ApiModelProperty(value = "删除标记", hidden = true)
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

    public <T extends LogicalDataEntity> T setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return (T) this;
    }
}
