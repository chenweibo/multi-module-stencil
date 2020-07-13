package com.chen.stencil.common.response;

import lombok.ToString;

/**
 * 枚举了一些常用API操作码
 *
 * @author chen
 * @date 2019/4/19
 */
@ToString
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_PARAMS(20003, "请求类型参数检验失败,请求类型是否正确"),
    VALIDATE_FAILED(20004, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    BIG_SIZE(442, "文件上传限制10M");


    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
