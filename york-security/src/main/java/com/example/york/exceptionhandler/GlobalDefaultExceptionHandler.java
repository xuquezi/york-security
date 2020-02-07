package com.example.york.exceptionhandler;

import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.ResponseResult;
import com.example.york.exception.SelfThrowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice// @ResponseBody+@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    // 捕捉运行时所有异常
    @ExceptionHandler(SelfThrowException.class)
    public ResponseResult selfThrowExceptionHandler(SelfThrowException e){
        log.error("系统内部异常，异常信息：", e);
        //返回ResponseCode.REQUEST_FAIL，会导致前端弹框提示
        return new ResponseResult(e.getMessage(), ResponseCode.REQUEST_FAIL);
    }

    // 捕捉运行时所有异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult runtimeExceptionHandler(RuntimeException e){
        log.error("系统内部异常，异常信息：", e);
        //返回ResponseCode.RUNTIME_EXCEPTION20005，会导致前端跳转500页面，而不只是弹框提示
        return new ResponseResult("服务器运行异常,请联系管理员!", ResponseCode.RUNTIME_EXCEPTION);
    }

    // 捕捉所有异常，例如IOException不属于RuntimeException，在这里处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        log.error("系统内部异常，异常信息：", e);
        //返回ResponseCode.RUNTIME_EXCEPTION20005，会导致前端跳转500页面，而不只是弹框提示
        return new ResponseResult("服务器运行异常,请联系管理员!", ResponseCode.RUNTIME_EXCEPTION);
    }

}
