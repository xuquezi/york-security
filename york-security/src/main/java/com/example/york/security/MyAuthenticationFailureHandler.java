package com.example.york.security;

import com.alibaba.fastjson.JSONObject;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//登录验证失败的处理类
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("执行到MyAuthenticationFailureHandler:登录验证失败的处理类");
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSONObject.toJSONString(new ResponseResult("验证失败,密码或者用户名错误!", ResponseCode.LOGIN_FAIL));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
