package com.example.york.service;

import com.example.york.entity.FlowMain;

import java.util.List;

public interface FlowMainService {
    void saveFlowMain(FlowMain flowMain);

    void updateFlowMain(FlowMain flowMain);

    List<FlowMain> getApplyingProcessByUserId(String currentUserId, Integer start, Integer limit);

    Integer countApplyingProcessByUserId(String currentUserId);

}
