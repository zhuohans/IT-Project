package com.haste.yzx.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "success"),
    FAIL(400, "fail"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    INTERNAL_SERVER_ERROR(500, "internal server error"),
    BAD_REQUEST(400, "bad request"),
    UNPROCESSABLE_ENTITY(422, "unprocessable entity"),
    UNDEFINED_EXCEPTION(40000,"undefined exception"),
    USERNAME_PASSWORD_EXCEPTION(40001,"username password exception"),
    USER_NOT_REGISTER_EXCEPTION(40002,"user not register exception"),
    PASSWORD_FORMAT_ERROR(40003,"password format error"),
    SQL_INTEGRITY_EXCEPTION(40004,"sql integrity exception"),
    VERIFICATION_CODE_EXCEPTION(40005,"verification code exception"),
    USER_EXISTS(40006,"user exists"),
    VERIFICATION_CODE_EXPIRED(40007,"verification code expired"),
    PARAMS_VALID_EXCEPTION(40020,"params valid exception"),
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
