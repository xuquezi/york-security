package com.example.york.security;

import com.alibaba.fastjson.JSONObject;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义没有权限的处理类
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.info("执行到RestAccessDeniedHandler：没有权限的处理类");
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSONObject.toJSONString(new ResponseResult("没有权限", ResponseCode.NO_AUTHORIZATION));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
