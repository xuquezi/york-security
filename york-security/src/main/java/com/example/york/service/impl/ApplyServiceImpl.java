package com.example.york.service.impl;

import com.example.york.constant.Const;
import com.example.york.constant.ProcessConst;
import com.example.york.dao.LeaveApplyMapper;
import com.example.york.entity.*;
import com.example.york.entity.activiti.ProcessTask;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.ApplyService;
import com.example.york.service.DepartmentService;
import com.example.york.service.FlowMainService;
import com.example.york.service.UserService;
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
        // 设置参数end
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey,map);
    }

    /**
     * 分页查询请假流程等待申请列表数据
     * @param search
     * @param limit
     * @param page
     * @param currentUserId
     * @param processType
     * @return
     */
    @Override
    public PageInfo getLeaveWaitApplyList(String search, Integer limit, Integer page, String currentUserId,String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(currentUserId) //操作用户
                .taskNameLike("%" + search + "%")
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY) //阶段的key
                .processDefinitionKey(processType) //类型
                .orderByTaskCreateTime()
                .desc().listPage(start, limit);
        long count = taskService.createTaskQuery()
                .taskAssignee(currentUserId)
                .taskNameLike("%" + search + "%")
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPLY)
                .processDefinitionKey(processType)
                .count();
        List<ProcessTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    /**
     * 请假流程提交shenq
     * @param leaveApply
     */
    @Override
    public void applyLeaveProcess(LeaveApply leaveApply) {
        String approveUserId = "";
        String approveUsername = "";
        String[] approveUser = null;

        // 当前阶段操作人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();

        //获取下一阶段的审批人
        String leaveApplyDepartmentId = leaveApply.getLeaveApplyDepartmentId();
        approveUser = this.getApproveUser(leaveApplyDepartmentId).split("@");
        if(approveUser.length>1){
            approveUserId=approveUser[0];
            approveUsername = approveUser[1];
        }else if(approveUser.length==1){
            approveUserId=approveUser[0];
        }
        //如果当前用户和审批用户一致，一般是当前用户就是部门的管理者，这时需要再取一下上级领导作为下一级审批人。
        if((currentUserId).equals(approveUserId)){
            Department department = departmentService.getDepartmentById(leaveApplyDepartmentId);
            approveUser = this.getApproveUser(department.getParentDepartmentSerial()).split("@");
            if(approveUser.length>1){
                approveUserId=approveUser[0];
                approveUsername = approveUser[1];
            }else if(approveUser.length==1){
                approveUserId=approveUser[0];
            }
        }
        Map<String , Object> map = new HashMap<>();
        map.put(ProcessConst.LEAVE_PROCESS_APPLY,Const.AGREE);
        // 下一阶段操作人
        map.put("userId",approveUserId);
        map.put("username",approveUsername);
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        map.put("applyUserId",currentUserId);
        map.put("applyUsername",currentUserName);
        String remark = leaveApply.getLeaveApplyRemark();
        if(StringUtils.isNotEmpty(remark)){
            map.put("remark",remark);
        }
        taskService.complete(leaveApply.getProcessTaskId(),map);
        //  保存申请记录
        LeaveApply leaveApplyData = leaveApplyMapper.getLeaveApplyData(leaveApply.getProcessTaskInstanceId(),leaveApply.getProcessTaskDefinitionId());
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
     * @param processTaskId
     */
    @Override
    public void cancelProcess(String processTaskId) {
        // 当前阶段操作人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        map.put(ProcessConst.LEAVE_PROCESS_APPLY,Const.DISAGREE);
        taskService.complete(processTaskId,map);
    }

    @Override
    public void agreeLeaveApply (ApproveResult approveResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String currentUserId = userDetail.getUserId();
        String currentUserName = userDetail.getUsername();
        Map<String , Object> map = new HashMap<>();
        // 当前阶段操作人
        map.put("currentUserId",currentUserId);
        map.put("currentUsername",currentUserName);
        map.put(ProcessConst.LEAVE_PROCESS_APPROVE,Const.AGREE);
        String approveRemark = approveResult.getApproveRemark();
        if(StringUtils.isNotEmpty(approveRemark)){
            map.put("remark",approveRemark);
        }
        taskService.complete(approveResult.getProcessTaskId(),map);
    }

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
        String approveRemark = approveResult.getApproveRemark();
        if(StringUtils.isNotEmpty(approveRemark)){
            map.put("remark",approveRemark);
        }
        taskService.complete(approveResult.getProcessTaskId(),map);
    }

    @Override
    public PageInfo getApplyingList(Integer limit, Integer page, String currentUserId) {
        List<Task> taskList = new ArrayList<>();
        Integer start = (page-1)*limit;
        List<FlowMain> list = flowMainService.getApplyingProcessByUserId(currentUserId,start,limit);
        Integer total = flowMainService.countApplyingProcessByUserId(currentUserId);
        if(list!=null&&list.size()>0){
            for (FlowMain flowMain : list) {
                Task task = taskService.createTaskQuery()
                        .processDefinitionId(flowMain.getProcessDefinitionId())
                        .processInstanceId(flowMain.getProcessInstanceId()).singleResult();
                taskList.add(task);
            }
        }
        List<ProcessTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(total,processTaskList);
    }

    @Override
    public PageInfo getLeaveWaitApproveList(String search, Integer limit, Integer page, String currentUserId, String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(currentUserId + "") //操作用户
                .taskNameLike("%" + search + "%")
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPROVE) //阶段的key
                .processDefinitionKey(processType) //类型
                .orderByTaskCreateTime()
                .desc().listPage(start, limit);
        long count = taskService.createTaskQuery()
                .taskAssignee(currentUserId + "")
                .taskNameLike("%" + search + "%")
                .taskDefinitionKey(ProcessConst.LEAVE_PROCESS_APPROVE) //阶段的key
                .processDefinitionKey(processType)
                .count();
        List<ProcessTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    @Override
    public LeaveApply getLeaveApplyData(String processTaskInstanceId, String processTaskDefinitionId) {
        return leaveApplyMapper.getLeaveApplyData(processTaskInstanceId,processTaskDefinitionId);
    }


    /**
     * 获取上级审批人，如果当前机构没有，会去上级机构寻找审批人。
     * 如果上级机构也没有的话，就报错！
     * @param departmentId
     * @return
     */
    private String getApproveUser (String departmentId){
        Department department = departmentService.getDepartmentById(departmentId);
        if(department == null){
            throw new SelfThrowException("获取上级审批人失败！");
        }
        if(department != null&&StringUtils.isEmpty(department.getManagerUserSerial())&& StringUtils.isEmpty(department.getParentDepartmentSerial())){
            throw new SelfThrowException("获取上级审批人失败！");
        }
        if(department != null&&StringUtils.isEmpty(department.getManagerUserSerial())&&StringUtils.isNotEmpty(department.getParentDepartmentSerial())){
            return this.getApproveUser(department.getParentDepartmentSerial());
        }
        return department.getManagerUserSerial()+"@"+department.getUserInfo().getUsername();
    }

    /**
     * 转化Task到ProcessTask
     * @param taskList
     * @return
     */
    private List<ProcessTask> transferTaskList(List<Task> taskList){
        List<ProcessTask> list = new ArrayList<>();
        for (Task task : taskList) {
            ProcessTask processTask = new ProcessTask();
            processTask.setProcessTaskDefinitionId(task.getProcessDefinitionId());
            processTask.setProcessTaskId(task.getId());
            processTask.setProcessTaskCreateTime(task.getCreateTime());
            processTask.setProcessTaskName(task.getName());
            processTask.setProcessTaskInstanceId(task.getProcessInstanceId());
            String assignee = task.getAssignee();
            processTask.setProcessTaskAssignee(assignee);
            if(StringUtils.isNotEmpty(assignee)){
                String username = userService.queryUsernameByUserSerial(assignee);
                if(username == null){
                    throw new SelfThrowException("获取用户失败，用户已经失效或删除！");
                }
                processTask.setProcessTaskAssigneeName(username);
            }
            list.add(processTask);
        }
        return list;
    }
}
