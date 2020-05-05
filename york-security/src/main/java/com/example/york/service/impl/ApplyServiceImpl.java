package com.example.york.service.impl;

import com.example.york.constant.Const;
import com.example.york.constant.ProcessConst;
import com.example.york.dao.LeaveApplyMapper;
import com.example.york.entity.*;
import com.example.york.entity.activiti.ActRuTask;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.*;
import com.example.york.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ApplyServiceImpl implements ApplyService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LeaveApplyMapper leaveApplyMapper;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FlowMainService flowMainService;
    @Autowired
    private UserService userService;
    @Autowired
    private FlowTaskService flowTaskService;


    /**
     * 开启流程
     * @param processDefKey
     */
    @Override
    public void startApplyByKey(String processDefKey) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        // 下一阶段操作人
        //注意这里设置的都是全局的变量，流程实例没有结束这个变量都是存在的。
        map.put("userId",currentUserId);
        map.put("username",currentUserName);
        // 当前阶段操作人
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        map.put("applyUserId",currentUserId);
        map.put("applyUsername",currentUserName);
        map.put("flowType",processDefKey);
        map.put("flowOperation",Const.FLOW_SUBMIT);
        // 设置参数end
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey,map);
    }

    /**
     * 分页查询被退回的流程
     * @param limit
     * @param page
     * @param currentUserId
     * @param processType
     * @return
     */
    @Override
    public PageInfo queryLeaveBackApplyListByPage(Integer limit, Integer page, String currentUserId, String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(currentUserId) //操作用户
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY) //阶段的key
                .processDefinitionKey(processType) //类型
                //为了区分被退回的申请，加入flowOperation变量，每次提交或者退回或者取消
                //都会更新flowOperation全局变量。
                .processVariableValueEquals("flowOperation",Const.FLOW_BACK)
                .orderByTaskCreateTime()
                .desc().listPage(start, limit);
        long count = taskService.createTaskQuery()
                .taskAssignee(currentUserId)
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY) //阶段的key
                .processVariableValueEquals("flowOperation",Const.FLOW_BACK)
                .processDefinitionKey(processType)
                .count();
        List<ActRuTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    /**
     * 获取流程申请审批过程的步骤详情
     * @param processDefinitionId
     * @param processInstanceId
     * @return
     */
    @Override
    public List<ProcessFlowDetail> queryProcess(String processDefinitionId, String processInstanceId) {
        return flowTaskService.queryProcess(processDefinitionId,processInstanceId);
    }


    /**
     * 分页查询请假流程等待申请列表数据
     * @param limit
     * @param page
     * @param currentUserId
     * @param processType
     * @return
     */
    @Override
    public PageInfo getLeaveWaitApplyList(Integer limit, Integer page, String currentUserId,String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(currentUserId) //操作用户
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY) //阶段的key
                .processDefinitionKey(processType) //类型
                //为了区分被退回的申请，加入flowOperation变量，每次提交或者退回或者取消
                //都会更新flowOperation全局变量。
                .processVariableValueEquals("flowOperation",Const.FLOW_SUBMIT)
                .orderByTaskCreateTime()
                .desc().listPage(start, limit);
        long count = taskService.createTaskQuery()
                .taskAssignee(currentUserId)
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY) //阶段的key
                .processDefinitionKey(processType)
                .processVariableValueEquals("flowOperation",Const.FLOW_SUBMIT)
                .count();
        List<ActRuTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    /**
     * 请假流程提交shenq
     * @param leaveApply
     */
    @Override
    public void applyLeaveProcess(LeaveApply leaveApply) {

        // 当前阶段操作人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();

        //获取下一阶段的审批人
        String leaveApplyDepartmentId = leaveApply.getLeaveApplyDepartmentId();
        // 根据部门Id获取部门经理
        UserInfo userInfo = userService.getDepartmentManagerUserSerial(leaveApplyDepartmentId);
        if(userInfo==null){
            throw new SelfThrowException("获取审批人失败!");
        }
        //如果当前用户和审批用户一致，一般是当前用户就是部门的管理者，这时需要再取一下上级领导作为下一级审批人。
        if((currentUserId).equals(userInfo.getUserSerial())){
            Department department = departmentService.getDepartmentById(leaveApplyDepartmentId);
            userInfo = userService.getDepartmentManagerUserSerial(department.getParentDepartmentSerial());
            if(userInfo==null){
                throw new SelfThrowException("获取审批人失败!");
            }
        }
        Map<String , Object> map = new HashMap<>();
        map.put(ProcessConst.LEAVE_PROCESS_APPLY,Const.AGREE);
        // 下一阶段操作人
        map.put("userId", userInfo.getUserSerial());
        map.put("username",userInfo.getUsername());
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        map.put("applyUserId",currentUserId);
        map.put("applyUsername",currentUserName);
        map.put("flowOperation",Const.FLOW_SUBMIT);
        String remark = leaveApply.getLeaveApplyRemark();
        if(StringUtils.isNotEmpty(remark)){
            map.put("remark",remark);
        }
        taskService.complete(leaveApply.getTaskId(),map);
        //  保存申请记录
        LeaveApply leaveApplyData = leaveApplyMapper.getLeaveApplyData(leaveApply.getTaskInstanceId(),leaveApply.getTaskDefinitionId());
        if(leaveApplyData!=null){
            leaveApply.setLeaveApplyId(leaveApplyData.getLeaveApplyId());
            // 已经存在的话说明之前申请过，是被退回的，直接更新不要新增。
            leaveApplyMapper.updateLeaveApply(leaveApply);
        }else {
            leaveApply.setLeaveApplyId("LA"+ UUIDUtil.getUUID());
            leaveApplyMapper.saveLeaveApply(leaveApply);
        }

    }

    /**
     * 取消请假流程申请
     * @param taskId
     */
    @Override
    public void cancelProcess(String taskId) {
        // 当前阶段操作人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        /**
         * 下一阶段操作人
         * 说明一下这里为什么要设置，原因是这里设置的都是全局变量。
         * 如果这里不设置下一阶段的处理人，那么全局变量中的userId和username仍然是原值
         * 导致listener中存储更新flow_task和flow_main有些问题。
         */
        map.put("userId",Const.CANCEL);
        map.put("username",Const.CANCEL);
        map.put(ProcessConst.LEAVE_PROCESS_APPLY,Const.DISAGREE);
        map.put("flowOperation",Const.FLOW_CANCEL);
        taskService.complete(taskId,map);
    }

    /**
     * 分页查询被取消的流程
     * @param limit
     * @param page
     * @param currentUserId
     * @return
     */
    @Override
    public PageInfo queryCancelApplyListByPage(Integer limit, Integer page, String currentUserId) {
        Integer start = (page-1)*limit;
        List<FlowMain> list = flowMainService.queryCancelApplyListByPage(currentUserId,start,limit);
        Integer total = flowMainService.countCancelApplyList(currentUserId);
        return new PageInfo(total,list);
    }

    /**
     * 分页查询审批完成的流程
     * @param limit
     * @param page
     * @param currentUserId
     * @return
     */
    @Override
    public PageInfo queryFinishApplyListByPage(Integer limit, Integer page, String currentUserId) {
        Integer start = (page-1)*limit;
        List<FlowMain> list = flowMainService.queryFinishApplyListByPage(currentUserId,start,limit);
        Integer total = flowMainService.countFinishApplyList(currentUserId);
        return new PageInfo(total,list);
    }


    /**
     * 分页查询申请中流程数据
     * @param limit
     * @param page
     * @param currentUserId
     * @return
     */
    @Override
    public PageInfo queryApplyingListByPage(Integer limit, Integer page, String currentUserId) {
        Integer start = (page-1)*limit;
        List<FlowMain> list = flowMainService.queryApplyingListByPage(currentUserId,start,limit);
        Integer total = flowMainService.countApplyingList(currentUserId);
        return new PageInfo(total,list);
    }

    /**
     * 获取申请的数据
     * @param taskInstanceId
     * @param taskDefinitionId
     * @return
     */
    @Override
    public LeaveApply getLeaveApplyData(String taskInstanceId, String taskDefinitionId) {
        return leaveApplyMapper.getLeaveApplyData(taskInstanceId,taskDefinitionId);
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
