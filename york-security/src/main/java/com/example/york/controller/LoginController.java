package com.example.york.controller;

import com.example.york.entity.AuthenticationBean;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
public class LoginController {

    @PostMapping("/login")
    @ApiOperation(value = "登入身份验证（JWT验证）", notes = "登入身份验证")
    //AuthenticationBean
    @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "AuthenticationBean",paramType = "body")
    public void login(@RequestBody AuthenticationBean user) {
        // 这里面不需要写任何代码，由UserDeatilsService去处理
        // 只是用于swagger页面展示api
    }
}
