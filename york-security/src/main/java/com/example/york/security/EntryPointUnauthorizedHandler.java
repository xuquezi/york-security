package com.example.york.security;

import com.alibaba.fastjson.JSONObject;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义用户未登录处理类
@Component
@Slf4j
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("执行到EntryPointUnauthorizedHandler：用户未登录处理类");
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSONObject.toJSONString(new ResponseResult("验证失败", ResponseCode.Token_Expired));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();

    }
}
