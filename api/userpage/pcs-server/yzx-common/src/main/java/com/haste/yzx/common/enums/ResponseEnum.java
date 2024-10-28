package com.haste.yzx.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "success"),
    FAIL(400, "fail"),
    UNAUTHORIZED(401, "unauthorized"),// Unauthorized
    FORBIDDEN(403, "forbidden"),// Forbidden
    NOT_FOUND(404, "not found"), // Not found
    INTERNAL_SERVER_ERROR(500, "internal server error"),// Internal server error
    BAD_REQUEST(400, "bad request"),// Bad request
    UNPROCESSABLE_ENTITY(422, "unprocessable entity"),// Data format error
    UNDEFINED_EXCEPTION(40000,"undefined error"),
    USERNAME_PASSWORD_EXCEPTION(40001,"用户名密码错误"),
    USER_NOT_REGISTER_EXCEPTION(40002,"用户未注册"),
    PASSWORD_FORMAT_ERROR(40003,"密码格式错误"),
    SQL_INTEGRITY_EXCEPTION(40004,"违反数据库数据完整性约束"),
    VERIFICATION_CODE_EXCEPTION(40005,"验证码错误"),
    USER_EXISTS(40006,"用户已存在"),
    VERIFICATION_CODE_EXPIRED(40007,"验证码已过期"),
    PARAMS_VALID_EXCEPTION(40020,"参数校验错误"),
    ;

    public Integer code;
    public String msg;

    private Integer getCode() {
        return this.code;
    }

    private String getMsg(){
        return this.msg;
    }

    public static String getMsg(Integer code){
        for (ResponseEnum responseEnum : ResponseEnum.values()) {
            if (responseEnum.getCode().equals(code)) {
                return responseEnum.getMsg();
            }
        }
        return null;
    }
}
