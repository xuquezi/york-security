package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskSysLog {
    // 操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date visitTime;
    // 耗时（毫秒）
    private Long executionTime;
    // 执行方法
    private String method;

    private Integer taskId;
    //任务描述
    private String taskDescribe;
    //任务的cron表达式
    private String cron;
    //任务名称
    private String taskName;

}
