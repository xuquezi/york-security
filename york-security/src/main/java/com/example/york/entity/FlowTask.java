package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowTask {

    // 流程定义id
    private String processDefinitionId;
    // 流程实例id
    private String processInstanceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date flowExecuteTime;
    // 主键
    private String flowTaskId;
    //执行用户名
    private String flowUserName;
    //执行用户id
    private String flowUserId;
    // 流程意见
    private String flowOpinion;
    //流程阶段
    private String flowPhase;

    private String flowRemark;
    //下一阶段处理人id
    private String nextUserId;
    //下一阶段处理人username
    private String nextUsername;






}
