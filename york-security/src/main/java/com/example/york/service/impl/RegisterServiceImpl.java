package com.example.york.service.impl;

import com.aliyuncs.CommonResponse;
import com.example.york.constant.Const;
import com.example.york.entity.MessageLog;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.MessageLogService;
import com.example.york.service.RegisterService;
import com.example.york.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageLogService messageLogService;
    @Autowired
    private SmsUtil smsUtil;
    @Override
    public CommonResponse sendCode(String tel) {
        try{
            //生成一个6位数的随机验证码
            String code = String.valueOf((long) (Math.random() * 1000000));
            //将string转化为json格式，code的键对应的是短信模板里面的 ${code}
            String jsonCode ="{\"code\":"+code+"}";
            //将验证码存入redis
            //使用Redis的Hash数据机构
            //保证主键唯一，使用uuid+前缀
            String redisKey = "Code"+UUID.randomUUID().toString();
            redisTemplate.boundHashOps(redisKey).put(tel, code);
            //设置验证码的过期时间5分钟
            redisTemplate.boundHashOps(redisKey).expire(5, TimeUnit.MINUTES);

            CommonResponse commonResponse = smsUtil.sendSms(tel, Const.TEMPLATE_CODE, Const.SIGN_NAME, jsonCode);
            //存入信息发送的日志表中start
            this.saveMessage(redisKey,tel,code);
            //存入信息发送的日志表中end
            return commonResponse;
        }catch (Exception e){
            log.error("系统内部异常，异常信息：", e);
            throw new SelfThrowException("服务器运行异常，发送验证码失败！请联系管理员！");
        }
    }

    private void saveMessage(String redisKey,String tel,String code){
        MessageLog messageLog = new MessageLog();
        messageLog.setMessageId(redisKey);
        messageLog.setSendDate(new Date());
        messageLog.setMobile(tel);
        messageLog.setSendFlag(0);//状态已发送
        String content = "您正在申请手机注册，验证码为："+code+"，5分钟内有效！";
        messageLog.setContent(content);
        messageLogService.saveMessageLog(messageLog);
    }
}
