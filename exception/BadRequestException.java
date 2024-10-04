package com.haste.yzx.common.exception;

import lombok.Getter;

/**
 * Bad request exception defined
 */
@Getter
public class BadRequestException extends RuntimeException{
    private Integer code;
    public BadRequestException(Integer code,String msg){
        super(msg);
        this.code = code;
    }
}