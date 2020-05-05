package com.example.york.service;

import com.example.york.entity.FlowMain;

import java.util.List;

public interface FlowMainService {
    void saveFlowMain(FlowMain flowMain);

    void updateFlowMain(FlowMain flowMain);

    List<FlowMain> queryApplyingListByPage(String currentUserId, Integer start, Integer limit);

    Integer countApplyingList(String currentUserId);

    List<FlowMain> queryCancelApplyListByPage(String currentUserId, Integer start, Integer limit);

    Integer countCancelApplyList(String currentUserId);

    List<FlowMain> queryFinishApplyListByPage(String currentUserId, Integer start, Integer limit);

    Integer countFinishApplyList(String currentUserId);
}
