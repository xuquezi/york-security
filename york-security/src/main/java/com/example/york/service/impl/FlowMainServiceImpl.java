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

    /**
     * 插入保存flowMain
     * @param flowMain
     */
    @Override
    public void saveFlowMain(FlowMain flowMain) {
        flowMainMapper.saveFlowMain(flowMain);

    }

    /**
     * 更新flowMain
     * @param flowMain
     */
    @Override
    public void updateFlowMain(FlowMain flowMain) {
        flowMainMapper.updateFlowMain(flowMain);
    }

    /**
     * 分页查询申请中的流程
     * @param currentUserId
     * @param start
     * @param limit
     * @return
     */
    @Override
    public List<FlowMain> queryApplyingListByPage(String currentUserId, Integer start, Integer limit) {
       return flowMainMapper.queryApplyingListByPage(currentUserId,start,limit);
    }

    /**
     * 查询申请中的流程数量
     * @param currentUserId
     * @return
     */
    @Override
    public Integer countApplyingList(String currentUserId) {
        return flowMainMapper.countApplyingList(currentUserId);
    }

    /**
     * 分页查询被取消流程
     * @param currentUserId
     * @param start
     * @param limit
     * @return
     */
    @Override
    public List<FlowMain> queryCancelApplyListByPage(String currentUserId, Integer start, Integer limit) {
        return flowMainMapper.queryCancelApplyListByPage(currentUserId,start,limit);
    }

    /**
     * 查询被取消的流程数量
     * @param currentUserId
     * @return
     */
    @Override
    public Integer countCancelApplyList(String currentUserId) {
        return flowMainMapper.countCancelApplyList(currentUserId);
    }

}
