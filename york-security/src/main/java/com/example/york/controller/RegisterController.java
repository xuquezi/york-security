package com.example.york.controller;

import com.example.york.constant.ResponseCode;
import com.example.york.entity.User;
import com.example.york.entity.result.BooleanResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.entity.result.StringResult;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.RegisterService;
import com.example.york.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RedisTemplate redisTemplate;

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

    @GetMapping("/validateTel")
    @ApiImplicitParam(name = "tel", value = "电话号码", required = true, dataType = "String",paramType = "query")
    @ApiOperation(value="注册时校验电话号码有没有已经被注册", notes="注册时验证电话号码有没有已经被注册")
    public BooleanResult validateTel(@RequestParam(name = "tel",defaultValue = "",required = true)String tel){
        if(StringUtils.isEmpty(tel)){
            throw new SelfThrowException("电话号码为空！");
        }
        Boolean flag = userService.validateTel(tel);
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
    public StringResult sendCode(@RequestParam(name = "tel",defaultValue = "",required = true)String tel){
        if(StringUtils.isEmpty(tel)){
            //其实这里不需要，因为我这边前端有控制不能传空号码过来.
            throw new SelfThrowException("电话号码为空！");
        }
        String redisKey = registerService.sendCode(tel);
        //返回验证码的redis主键给前端
        StringResult stringResult = new StringResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        stringResult.setKey(redisKey);

        return stringResult;
    }

    @PostMapping("/registerUser")
    @ApiOperation(value="注册用户", notes="注册用户")
    @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "User",paramType = "body")
    public ResponseResult login(@RequestBody User user) {
        //System.out.println(user);
        //注册用户插入数据，默认用户启用，出生日期为空默认当前日期
        if(StringUtils.isEmpty(user.getUsername())){
            throw new SelfThrowException("用户名为空!");//防止前端传空值
        }else if(StringUtils.isEmpty(user.getEmail())){
            throw new SelfThrowException("邮箱为空!");//防止前端传空值
        }else if(StringUtils.isEmpty(user.getTel())){
            throw new SelfThrowException("电话号码为空!");
        }
        //log.info(user.getCode());
        String code = user.getCode();
        String key = user.getKey();
        String redisCode = (String) redisTemplate.boundValueOps(key).get();
        //log.info(redisCode);
        if(!code.equals(redisCode)){
            throw new SelfThrowException("验证码有误!");
        }
        //保存用户
        saveUser(user);
        return new ResponseResult("请求成功", ResponseCode.REQUEST_SUCCESS);
    }

    private void saveUser(User user){
        user.setStatus(0);//默认启用
        user.setCreateTime(new Date());
        user.setCreateUser(user.getUsername());
        user.setDeleteStatus(0);//默认逻辑删除为0
        //密码进行加密start
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        //密码进行加密end
        if(user.getBorn()==null){
            //没有值默认出生日期为当天
            user.setBorn(new Date());
        }
        userService.saveUser(user);

    }


}
