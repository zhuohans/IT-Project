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
        log.error("Undefined error: {}, Error details: {}", e.getCause(), e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.UNDEFINED_EXCEPTION.code;
        return Response.error(code, message);
    }

    @ResponseBody
    @ExceptionHandler(value = NotLoginException.class)
    public Response notLoginException(NotLoginException e) {
        log.error("Token error: {}, Error details: {}", e.getCode(), e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code, message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public Response badRequestException(BadRequestException e) {
        log.error("Error code: {}, Error details: {}", e.getCode(), e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code, message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadPaddingException.class)
    public Response badRequestException(BadPaddingException e) {
        log.error("Error code: {}, Error details: {}", "Password format error", e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.PASSWORD_FORMAT_ERROR.code;
        return Response.error(code, message);
    }

    @ResponseBody
    @ExceptionHandler(value = SQLSyntaxErrorException.class)
    public Response handleSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        log.error("SQL syntax error!", e);
        String message = e.getMessage();
        if (StrUtil.isNotEmpty(message) && message.contains("denied")) {
            message = "SQL syntax error!";
        }
        return Response.error(message);
    }
}