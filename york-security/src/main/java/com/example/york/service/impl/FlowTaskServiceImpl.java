package com.example.york.service.impl;

import com.example.york.dao.FlowTaskMapper;
import com.example.york.entity.FlowTask;
import com.example.york.entity.ProcessFlowDetail;
import com.example.york.service.FlowTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FlowTaskServiceImpl implements FlowTaskService {
    @Autowired
    private FlowTaskMapper flowTaskMapper;

    /**
     * 保存flowTask
     * @param flowTask
     */
    @Override
    public void saveFlowTask(FlowTask flowTask) {
        flowTaskMapper.saveFlowTask(flowTask);
    }

    /**
     * 获取流程申请审批过程的步骤详情
     * @param processDefinitionId
     * @param processInstanceId
     * @return
     */
    @Override
    public List<ProcessFlowDetail> queryProcess(String processDefinitionId, String processInstanceId) {
        List<FlowTask> flowTaskList = flowTaskMapper.queryProcess(processDefinitionId,processInstanceId);
        List<ProcessFlowDetail> list = new ArrayList<>();
        for (FlowTask flowTask : flowTaskList) {
            ProcessFlowDetail flowDetail = new ProcessFlowDetail();
            String title = "流程类型："+flowTask.getFlowType()+"  流程阶段:"+flowTask.getFlowLastPhase();
            flowDetail.setTitle(title);
            String content = flowTask.getFlowUserName()+ "  于 "+ flowTask.getFlowExecuteTime()+"  "+flowTask.getFlowOperation();
            flowDetail.setContent(content);
            flowDetail.setTimestamp(flowTask.getFlowExecuteTime().toString());
            list.add(flowDetail);
        }
        return list;
    }
}
