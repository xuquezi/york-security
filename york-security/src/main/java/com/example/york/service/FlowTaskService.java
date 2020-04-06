package com.example.york.service;

import com.example.york.entity.FlowTask;
import com.example.york.entity.ProcessFlowDetail;

import java.util.List;

public interface FlowTaskService {
    void saveFlowTask(FlowTask flowTask);

    List<ProcessFlowDetail> queryProcess(String processDefinitionId, String processInstanceId);
}
