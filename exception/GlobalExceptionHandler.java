package com.haste.yzx.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.util.StrUtil;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;
import java.sql.SQLSyntaxErrorException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response allException(Exception e) {
        log.error("undefined error：{}，error detail：{}",e.getCause(),e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.UNDEFINED_EXCEPTION.code;
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = NotLoginException.class)
    public Response notLoginException(NotLoginException e) {
        log.error("token error：{}，error detail：{}",e.getCode(),e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public Response badRequestException(BadRequestException e) {
        log.error("error code：{}，error detail：{}",e.getCode(),e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadPaddingException.class)
    public Response badRequestException(BadPaddingException e) {
        log.error("error code：{}，error detail：{}","password format error",e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.PASSWORD_FORMAT_ERROR.code;
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = SQLSyntaxErrorException.class)
    public Response handleSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        log.error("SQL integrity exception！", e);
        String message = e.getMessage();
        if (StrUtil.isNotEmpty(message) && message.contains("denied")) {
            message = "SQL integrity exception！";
        }
        return Response.error(message);
    }
}
