package com.example.york.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ActRuTask {
    private String taskId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date taskCreateTime;

    private String taskName;
    // 流程实例id
    private String taskInstanceId;
    //当前审批人id
    private String taskAssignee;

    private String taskAssigneeName;
    // 流程定义id
    private String taskDefinitionId;

    private String taskExecutionId;

    private String taskDefKey;
}
