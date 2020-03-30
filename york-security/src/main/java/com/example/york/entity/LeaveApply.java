package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
//请假流程申请记录表
public class LeaveApply {
    private String leaveApplyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date leaveApplyTime;
    // 申请原因
    private String leaveApplyRemark;
    // 申请天数
    private Integer leaveApplyDays;
    // 申请用户id
    private String leaveApplyUserId;
    // 申请人姓名
    private String leaveApplyUsername;

    // 申请人所在部门id
    private String leaveApplyDepartmentId;
    // 申请人所在部门名称
    private String leaveApplyDepartmentName;

    //任务id
    private String processTaskId;

    //实例id
    private String processTaskInstanceId;
    // 流程定义id
    private String processTaskDefinitionId;

}
