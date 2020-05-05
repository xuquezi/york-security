package com.example.york.service.impl;

import com.example.york.constant.Const;
import com.example.york.constant.ProcessConst;
import com.example.york.entity.ApproveResult;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.entity.activiti.ActRuTask;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.ApproveService;
import com.example.york.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApproveServiceImpl implements ApproveService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    /**
     * 分页查询待审批流程
     * @param limit
     * @param page
     * @param currentUserId
     * @param processType
     * @return
     */
    @Override
    public PageInfo queryLeaveWaitApproveListByPage(Integer limit, Integer page, String currentUserId, String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(currentUserId) //操作用户
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPROVE) //阶段的key
                .processDefinitionKey(processType) //类型
                //为了区分被退回的申请，加入flowOperation变量，每次提交或者退回或者取消
                //都会更新flowOperation全局变量。
                .processVariableValueEquals("flowOperation", Const.FLOW_SUBMIT)
                .orderByTaskCreateTime()
                .desc().listPage(start, limit);
        long count = taskService.createTaskQuery()
                .taskAssignee(currentUserId)
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPROVE) //阶段的key
                .processDefinitionKey(processType)
                .processVariableValueEquals("flowOperation", Const.FLOW_SUBMIT)
                .count();
        List<ActRuTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    /**
     * 请假流程审批同意
     * @param approveResult
     */
    @Override
    public void agreeLeaveApply(ApproveResult approveResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        // 当前阶段操作人
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        /**
         * 下一阶段操作人
         * 说明一下这里为什么要设置，原因是这里设置的都是全局变量。
         * 如果这里不设置下一阶段的处理人，那么全局变量中的userId和username仍然是原值
         * 导致listener中存储更新flow_task和flow_main有些问题。
         */
        map.put("userId",Const.FINISH);
        map.put("username",Const.FINISH);
        map.put(ProcessConst.LEAVE_PROCESS_APPROVE,Const.AGREE);
        map.put("flowOperation",Const.FLOW_FINISH);
        String approveRemark = approveResult.getApproveRemark();
        if(StringUtils.isNotEmpty(approveRemark)){
            map.put("remark",approveRemark);
        }
        taskService.complete(approveResult.getProcessTaskId(),map);
    }

    /**
     * 请假流程退回到申请人
     * @param approveResult
     */
    @Override
    public void backLeaveApply(ApproveResult approveResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        // 当前阶段操作人
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        // 下一阶段操作人
        map.put("userId",approveResult.getApplyUserId());
        map.put("username",approveResult.getApplyUsername());
        map.put(ProcessConst.LEAVE_PROCESS_APPROVE,Const.DISAGREE);
        map.put("flowOperation",Const.FLOW_BACK);
        String approveRemark = approveResult.getApproveRemark();
        if(StringUtils.isNotEmpty(approveRemark)){
            map.put("remark",approveRemark);
        }
        taskService.complete(approveResult.getProcessTaskId(),map);
    }

    /**
     * 转化Task到ProcessTask
     * @param taskList
     * @return
     */
    private List<ActRuTask> transferTaskList(List<Task> taskList){
        List<ActRuTask> list = new ArrayList<>();
        for (Task task : taskList) {
            ActRuTask actRuTask = new ActRuTask();
            actRuTask.setTaskDefinitionId(task.getProcessDefinitionId());
            actRuTask.setTaskId(task.getId());
            actRuTask.setTaskCreateTime(task.getCreateTime());
            actRuTask.setTaskName(task.getName());
            actRuTask.setTaskInstanceId(task.getProcessInstanceId());
            String assignee = task.getAssignee();
            actRuTask.setTaskAssignee(assignee);
            actRuTask.setTaskExecutionId(task.getExecutionId());
            actRuTask.setTaskDefKey(task.getTaskDefinitionKey());
            if(StringUtils.isNotEmpty(assignee)){
                String username = userService.queryUsernameByUserSerial(assignee);
                if(username == null){
                    throw new SelfThrowException("获取用户失败，用户已经失效或删除！");
                }
                actRuTask.setTaskAssigneeName(username);
            }
            list.add(actRuTask);
        }
        return list;
    }
}
