package com.haste.yzx.common.response;

import com.haste.yzx.common.enums.ResponseEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response success(Object data) {
        return new Response(ResponseEnum.SUCCESS.code, ResponseEnum.SUCCESS.msg,data);
    }

    public static Response success() {
        return new Response(ResponseEnum.SUCCESS.code, ResponseEnum.SUCCESS.msg,null);
    }

    public static Response error() {
        return new Response(ResponseEnum.FAIL.code, ResponseEnum.FAIL.msg, null);
    }

    public static Response error(String msg) {
        return new Response(ResponseEnum.FAIL.code, msg, null);
    }

    public static Response error(Integer code,String msg) {
        return new Response(code, msg, null);
    }
}
