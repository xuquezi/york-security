package com.example.york.exceptionhandler;

import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice// @ResponseBody+@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    // 捕捉运行时所有异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult RuntimeExceptionHandler(RuntimeException e){
        log.error("系统内部异常，异常信息：", e);
        log.error(e.getMessage());
        return new ResponseResult(e.getMessage(), ResponseCode.REQUEST_FAIL);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    public ResponseResult globalExceptionHandler(Exception e){
        log.error("系统内部异常，异常信息：", e);
        return new ResponseResult(e.getMessage(), ResponseCode.REQUEST_FAIL);
    }

}
