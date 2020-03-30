package com.example.york.service;

import com.example.york.entity.ApproveResult;
import com.example.york.entity.LeaveApply;
import com.example.york.entity.PageInfo;

public interface ApplyService {
    void startApplyByKey(String processDefKey);

    PageInfo getLeaveWaitApplyList(String search, Integer limit, Integer page, String currentUserId,String processType);

    void applyLeaveProcess(LeaveApply leaveApply);

    void cancelProcess(String processTaskId);

    PageInfo getApplyingList(Integer limit, Integer page, String currentUserId);

    PageInfo getLeaveWaitApproveList(String search, Integer limit, Integer page, String currentUserId, String processType);

    LeaveApply getLeaveApplyData(String processTaskInstanceId, String processTaskDefinitionId);

    void agreeLeaveApply(ApproveResult approveResult);

    void backLeaveApply(ApproveResult approveResult)
            ;
}
