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
        log.error("未定义的错误：{}，错误详情：{}",e.getCause(),e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.UNDEFINED_EXCEPTION.code;
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = NotLoginException.class)
    public Response notLoginException(NotLoginException e) {
        log.error("token错误：{}，错误详情：{}",e.getCode(),e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public Response badRequestException(BadRequestException e) {
        log.error("错误代码：{}，错误详情：{}",e.getCode(),e.getMessage());
        String message = e.getMessage();
        Integer code = e.getCode();
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadPaddingException.class)
    public Response badRequestException(BadPaddingException e) {
        log.error("错误代码：{}，错误详情：{}","密码格式错误",e.getMessage());
        String message = e.getMessage();
        Integer code = ResponseEnum.PASSWORD_FORMAT_ERROR.code;
        return Response.error(code,message);
    }

    @ResponseBody
    @ExceptionHandler(value = SQLSyntaxErrorException.class)
    public Response handleSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        log.error("SQL语法错误！", e);
        String message = e.getMessage();
        if (StrUtil.isNotEmpty(message) && message.contains("denied")) {
            message = "SQL语法错误！";
        }
        return Response.error(message);
    }
}
