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
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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


    /*  String code = user.getCode();
        String key = user.getKey();
        String redisCode = (String) redisTemplate.boundValueOps(key).get();
        //log.info(redisCode);
        if(!code.equals(redisCode)){
            throw new SelfThrowException("验证码有误!");
        }*/
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //保存用户
        saveUser(user,encoder);
        //生成一个6位数的随机验证码
        String code = String.valueOf((long) (Math.random() * 1000000));
        //将验证码存入redis
        String redisKey = saveActiveCodeToRedis(code);
        // 发送邮箱激活信息
        sendActiveMail(user.getEmail(),code,user.getUsername(),redisKey,user.getUserId());

        return new ResponseResult("请求成功", ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/activeUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "redisKey", value = "redis存的key值", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int",paramType = "query")
    })
    @ApiOperation(value="注册后邮箱激活用户", notes="注册后邮箱激活用户")
    public ModelAndView activeUser(@RequestParam(name = "redisKey",required = true,defaultValue = "")String redisKey, @RequestParam(name = "code",required = true,defaultValue = "")String code,@RequestParam(name = "userId",required = true)Integer userId){
        log.info("用户id"+userId+"开始通过邮件激活用户！");
        ModelAndView mv = new ModelAndView();
        try {
            //http://tieguzhengzheng.top:8088/#/500
            if(redisTemplate.hasKey(redisKey)){
                String redisCode = (String) redisTemplate.opsForValue().get(redisKey);
                if(code.equals(redisCode)){
                    // 激活用户，更新用户状态为0
                    // test:int i= 1/0;
                    userService.activateUser(userId);
                    log.info("用户id"+userId+"通过邮件激活用户成功！");
                    mv.setViewName("redirect:http://localhost:9528/#/registerActiveSuccess");
                    return mv;
                }

            }
            log.info("用户id"+userId+"通过邮件激活用户失败，已经过了三分钟有效期！");
            mv.setViewName("redirect:http://localhost:9528/#/registerActiveError");
            return mv;
        }catch (Exception e){
            //遇到异常就直接跳转到500，而不是被全局异常处理返回json。
            log.error("系统内部异常，异常信息：", e);
            mv.setViewName("redirect:http://localhost:9528/#/500");
            return mv;
        }

    }

    private void saveUser(User user,BCryptPasswordEncoder encoder){
        // 用户注册需要邮件激活
        user.setStatus(2);//默认待激活
        user.setCreateTime(new Date());
        user.setCreateUser(user.getUsername());
        user.setDeleteStatus(0);//默认逻辑删除为0
        //密码进行加密start
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        //密码进行加密end
        if(user.getBorn()==null){
            //没有值默认出生日期为当天
            user.setBorn(new Date());
        }
        userService.saveUser(user);

    }



    //发送激活邮件
    private void sendActiveMail(String email, String code, String username, String redisKey,Integer userId) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom("1377262954@qq.com");
            mimeMessageHelper.setSubject("York用户注册激活邮件");

            StringBuilder sb = new StringBuilder();
            sb.append("<html><head></head>");
            sb.append("<body><h1>亲爱的"+username+"</h1><p>感谢您注册York，请在3分钟内点击下面的链接激活用户，完成注册！</p></body>");
            sb.append("<a href='http://localhost:1201/york/register/activeUser?redisKey="+redisKey+"&userId="+userId+"&code="+code+"' >点击激活，三分钟后失效！</a>");
            sb.append("</html>");

            // 启用html
            mimeMessageHelper.setText(sb.toString(), true);
            // 发送邮件
            mailSender.send(mimeMessage);
            log.info("发送给"+username+"的激活邮件成功！");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new SelfThrowException("发送给"+username+"的激活邮件失败！");
        }

    }

    private String saveActiveCodeToRedis(String code){
        //保证主键唯一，使用uuid+前缀
        String redisKey = "Code"+ UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(redisKey,code);
        //设置验证码的过期时间3分钟
        redisTemplate.expire(redisKey,3, TimeUnit.MINUTES);
        return redisKey;
    }


}
