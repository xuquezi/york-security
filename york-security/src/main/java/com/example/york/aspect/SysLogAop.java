package com.example.york.aspect;

import com.example.york.entity.SysLog;
import com.example.york.entity.User;
import com.example.york.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
@Slf4j
public class SysLogAop {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysLogService sysLogService;

    private Date visitTime; //开始时间
    private Class clazz; //访问的类
    private Method method;//访问的方法

    @Pointcut("@annotation(com.example.york.annotation.SysLog)")
    public void pointcut() {
        // do nothing
    }
    //前置通知  主要是获取开始时间，执行的类是哪一个，执行的是哪一个方法
    @Before("pointcut()")
    public void doBefore(JoinPoint jp) {
        visitTime = new Date();//当前时间就是开始访问的时间
        clazz = jp.getTarget().getClass(); //具体要访问的类
        MethodSignature signature = (MethodSignature) jp.getSignature();
        method = signature.getMethod();

    }


    //后置通知
    @After("pointcut()")
    public void doAfter(JoinPoint jp) {
        long time = new Date().getTime() - visitTime.getTime(); //获取访问的时长
        String url = "";
        //获取url
        //LogAop放在controller包内所以要排除
        if (clazz != null && method != null && clazz != SysLogAop.class) {
            //1.获取类上的@RequestMapping("/orders")
            RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (classAnnotation != null) {
                String[] classValue = classAnnotation.value();
                //2.获取方法上的@RequestMapping(xxx)
                RequestMapping requestMethodAnnotation = method.getAnnotation(RequestMapping.class);
                GetMapping getMethodAnnotation = method.getAnnotation(GetMapping.class);
                PostMapping postMethodAnnotation = method.getAnnotation(PostMapping.class);
                DeleteMapping deleteMethodAnnotation = method.getAnnotation(DeleteMapping.class);
                PutMapping putMethodAnnotation = method.getAnnotation(PutMapping.class);

                String methodUrl = getUrlFromMethodAnnotation(requestMethodAnnotation,getMethodAnnotation,postMethodAnnotation,deleteMethodAnnotation,putMethodAnnotation);

                url = classValue[0] + methodUrl;

                //获取访问的ip
                String ip = this.getIpAddr(request);

                //获取当前操作的用户
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userDetail = (User)authentication.getPrincipal();
                String username = userDetail.getUsername();
                log.info("操作用户名为："+username);

                //将日志相关信息封装到SysLog对象
                SysLog sysLog = new SysLog();
                sysLog.setExecutionTime(time); //执行时长
                sysLog.setIp(ip);
                sysLog.setMethod("[类名] " + clazz.getName() + "[方法名] " + method.getName());
                sysLog.setUrl(url);
                sysLog.setUsername(username);
                sysLog.setVisitTime(visitTime);

                //调用Service完成操作，保存sysLog
                sysLogService.save(sysLog);
            }
        }
    }

    /**
     * 获取 IP地址
     * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()获取 IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private String getUrlFromMethodAnnotation(RequestMapping requestMethodAnnotation,GetMapping getMethodAnnotation,PostMapping postMethodAnnotation,DeleteMapping deleteMethodAnnotation,PutMapping putMethodAnnotation){
        String methodUrl = "";
        if(getMethodAnnotation!= null){
            return getMethodAnnotation.value()[0];
        }
        else if(postMethodAnnotation!= null){
            return postMethodAnnotation.value()[0];
        }
        else if (putMethodAnnotation !=null){
            return putMethodAnnotation.value()[0];
        }
        else if(deleteMethodAnnotation !=null){
            return deleteMethodAnnotation.value()[0];
        }
        else if(requestMethodAnnotation !=null){
            return requestMethodAnnotation.value()[0];
        }
        //方法上没有映射地址的注解返回空字符串
        return methodUrl;

    }

}
