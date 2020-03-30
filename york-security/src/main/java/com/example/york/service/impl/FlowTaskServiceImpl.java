package com.example.york.service.impl;

import com.example.york.dao.FlowTaskMapper;
import com.example.york.entity.FlowTask;
import com.example.york.service.FlowTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class FlowTaskServiceImpl implements FlowTaskService {
    @Autowired
    private FlowTaskMapper flowTaskMapper;
    @Override
    public void saveFlowTask(FlowTask flowTask) {
        flowTaskMapper.saveFlowTask(flowTask);
    }
}
