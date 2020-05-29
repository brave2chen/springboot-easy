package com.github.brave2chen.springbooteasy.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * rest 统一返回对象
 *
 * @author chenqy28
 * @date 2020-05-29
 */
@Getter
public class RestResponse<T> {
    @ApiModelProperty(value = "信息", example = "操作成功")
    private String msg = "操作成功";

    @ApiModelProperty(value = "错误码", example = "B0001")
    private String code;

    private T data;

    private RestResponse(T data) {
        this.data = data;
    }

    private RestResponse(String code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static RestResponse fail(ErrorCode errorCode) {
        return new RestResponse(errorCode.name(), errorCode.getMsg(), null);
    }

    public static <T> RestResponse ok(T data) {
        return new RestResponse(data);
    }
}
