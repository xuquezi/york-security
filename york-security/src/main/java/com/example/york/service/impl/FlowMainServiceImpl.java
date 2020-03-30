package com.example.york.service.impl;

import com.example.york.dao.FlowMainMapper;
import com.example.york.entity.FlowMain;
import com.example.york.service.FlowMainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class FlowMainServiceImpl implements FlowMainService {
    @Autowired
    FlowMainMapper flowMainMapper;
    @Override
    public void saveFlowMain(FlowMain flowMain) {
        flowMainMapper.saveFlowMain(flowMain);

    }

    @Override
    public void updateFlowMain(FlowMain flowMain) {
        flowMainMapper.updateFlowMain(flowMain);
    }

    @Override
    public List<FlowMain> getApplyingProcessByUserId(String currentUserId, Integer start, Integer limit) {
       return flowMainMapper.getApplyingProcessByUserId(currentUserId,start,limit);
    }

    @Override
    public Integer countApplyingProcessByUserId(String currentUserId) {
        return flowMainMapper.countApplyingProcessByUserId(currentUserId);
    }


}
