package com.example.york.service;

import com.example.york.entity.ApproveResult;
import com.example.york.entity.LeaveApply;
import com.example.york.entity.PageInfo;
import com.example.york.entity.ProcessFlowDetail;

import java.util.List;

public interface ApplyService {
    void startApplyByKey(String processDefKey);

    PageInfo getLeaveWaitApplyList(Integer limit, Integer page, String currentUserId,String processType);

    void applyLeaveProcess(LeaveApply leaveApply);

    void cancelProcess(String taskId);

    PageInfo queryApplyingListByPage(Integer limit, Integer page, String currentUserId);

    PageInfo queryLeaveWaitApproveListByPage(Integer limit, Integer page, String currentUserId, String processType);

    LeaveApply getLeaveApplyData(String taskInstanceId, String taskDefinitionId);

    void agreeLeaveApply(ApproveResult approveResult);

    void backLeaveApply(ApproveResult approveResult)
            ;

    PageInfo queryCancelApplyListByPage(Integer limit, Integer page, String currentUserId);

    PageInfo queryLeaveBackApplyListByPage(Integer limit, Integer page, String currentUserId, String processType);

    List<ProcessFlowDetail> queryProcess(String processDefinitionId, String processInstanceId);

}
