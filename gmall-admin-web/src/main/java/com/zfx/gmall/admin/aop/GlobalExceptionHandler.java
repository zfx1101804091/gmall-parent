package com.zfx.gmall.admin.aop;

import com.zfx.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
*
*   统一处理所有异常，给前端返回500的json
*   当我们编写环绕通知的时候，目标方法出现的异常一定要再次抛出去
*
*   ControllerAdvice是全局异常注解
* */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ArithmeticException.class})
    public Object handlerException(Exception exception){

        //exception.getStackTrace() 从堆栈中获取所有异常
        log. error("系统全局异常感知，信息: {}",exception.getStackTrace());

        return new CommonResult() . validateFailed("数学设学好");
    }

    @ExceptionHandler(value = {NullPointerException. class})
    public Object handlerException2(Exception exception){

        log . error("系统出现异常感知，信息: {}" , exception. getMessage());

        return new CommonResult().validateFailed("空指针 了...");
    }

}
