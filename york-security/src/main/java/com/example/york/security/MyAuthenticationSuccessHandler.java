package com.example.york.security;

import com.alibaba.fastjson.JSONObject;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.LoginResult;
import com.example.york.service.LoginService;
import com.example.york.utils.CommonUtils;
import com.example.york.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//登录成功的处理类
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LoginService loginService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("执行到MyAuthenticationSuccessHandler：登录成功的处理类");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("用户 " + userDetails.getUsername() + " 登录成功");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //记录登入日志start
        String ip = CommonUtils.getIpAddr(request);
        // String ip = getIpAddr(request);
        loginService.saveLoginLog(userDetails.getUsername(),ip);
        //记录登入日志end
        String token = jwtTokenUtil.generateToken(userDetails);
        renderToken(response, token);

    }
    /**
     * 渲染返回 token 页面,因为前端页面接收的都是Result对象，故使用application/json返回
     *
     * @param response
     * @throws IOException
     */
    public void renderToken(HttpServletResponse response, String token) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSONObject.toJSONString(new LoginResult("验证成功", ResponseCode.LOGIN_SUCCESS,token));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
