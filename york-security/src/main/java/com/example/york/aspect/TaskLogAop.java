package com.example.york.aspect;

import com.example.york.annotation.TaskDescribe;
import com.example.york.annotation.TaskName;
import com.example.york.entity.TaskSysLog;
import com.example.york.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
@Slf4j
//切面类保存task执行的日志
public class TaskLogAop {
    @Autowired
    private TaskLogService taskLogService;

    private Date visitTime; //开始时间
    private Class clazz; //访问的类
    private Method method;//访问的方法

    @Pointcut("@annotation(com.example.york.annotation.TaskLog)")
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
        String cron = "";
        String taskDescribe = "";
        String taskName = "";

        Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class);
        if(scheduledAnnotation !=null ){
            //获取task的cron表达式
            cron = scheduledAnnotation.cron();
        }
        TaskDescribe taskDescribeAnnotation = method.getAnnotation(TaskDescribe.class);
        if(taskDescribeAnnotation != null){
            //获取task描述
            taskDescribe = taskDescribeAnnotation.value();
        }
        TaskName taskNameAnnotation = method.getAnnotation(TaskName.class);
        if(taskNameAnnotation != null){
            //获取task名称
            taskName = taskNameAnnotation.value();
        }


        TaskSysLog taskSysLog = new TaskSysLog();
        taskSysLog.setCron(cron);
        taskSysLog.setTaskDescribe(taskDescribe);
        taskSysLog.setMethod("[类名] " + clazz.getName() + "[方法名] " + method.getName());
        taskSysLog.setExecutionTime(time);
        taskSysLog.setVisitTime(visitTime);
        taskSysLog.setTaskName(taskName);

        taskLogService.saveTaskSysLog(taskSysLog);

    }
}
