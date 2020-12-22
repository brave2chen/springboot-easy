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

import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * LogicalEntity
 *
 * @author brave2chen
 */
@Getter
public abstract class VersionEntity extends BaseEntity {
    @ApiModelProperty(value = "版本号", hidden = true)
    @Version
    private Integer version;

    public <T extends VersionEntity> T setVersion(Integer version) {
        this.version = version;
        return (T) this;
    }
}
