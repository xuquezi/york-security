package com.example.york.controller;

import com.aliyuncs.CommonResponse;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.BooleanResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.RegisterService;
import com.example.york.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private RegisterService registerService;

    @GetMapping("/validateUsername")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",paramType = "query")
    @ApiOperation(value="注册时验证有无重复用户名", notes="注册时验证有无重复用户名")
    public BooleanResult validateUsername(@RequestParam(name = "username",defaultValue = "",required = true)String username){
        if(StringUtils.isEmpty(username)){
            throw new SelfThrowException("用户名为空！");
        }
        Boolean flag = userService.validateUsername(username);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        if(!flag){
            booleanResult.setFlag(false);
            return booleanResult;
        }
        booleanResult.setFlag(true);
        return booleanResult;
    }

    @GetMapping("/validateEmail")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String",paramType = "query")
    @ApiOperation(value="注册时验证邮箱有没有已经被注册", notes="注册时验证邮箱有没有已经被注册")
    public BooleanResult validateEmail(@RequestParam(name = "email",defaultValue = "",required = true)String email){
        if(StringUtils.isEmpty(email)){
            throw new SelfThrowException("邮箱为空！");
        }
        Boolean flag = userService.validateEmail(email);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        if(!flag){
            booleanResult.setFlag(false);
            return booleanResult;
        }
        booleanResult.setFlag(true);
        return booleanResult;
    }

    @GetMapping("/sendCode")
    @ApiImplicitParam(name = "tel", value = "电话", required = true, dataType = "String",paramType = "query")
    @ApiOperation(value="注册时发送验证码", notes="注册时发送验证码")
    public ResponseResult sendCode(@RequestParam(name = "tel",defaultValue = "",required = true)String tel){
        if(StringUtils.isEmpty(tel)){
            //其实这里不需要，因为我这边前端有控制不能传空号码过来.
            throw new SelfThrowException("电话号码为空！");
        }
        CommonResponse commonResponse = registerService.sendCode(tel);
        log.info(commonResponse.getData());
        //"Code":"OK"
        if(!commonResponse.getData().contains("OK")){
            throw new SelfThrowException("服务器运行异常，发送验证码失败！请联系管理员！");
        }
        ResponseResult responseResult = new ResponseResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        return responseResult;
    }
}
