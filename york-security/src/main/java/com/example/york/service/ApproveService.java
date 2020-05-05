package com.example.york.service;

import com.example.york.entity.ApproveResult;
import com.example.york.entity.PageInfo;

public interface ApproveService {
    PageInfo queryLeaveWaitApproveListByPage(Integer limit, Integer page, String currentUserId, String processType);

    void agreeLeaveApply(ApproveResult approveResult);

    void backLeaveApply(ApproveResult approveResult);
}
