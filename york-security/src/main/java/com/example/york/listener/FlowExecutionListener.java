package com.example.york.listener;

import com.example.york.entity.FlowMain;
import com.example.york.entity.FlowTask;
import com.example.york.service.FlowMainService;
import com.example.york.service.FlowTaskService;
import com.example.york.utils.UUIDUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class FlowExecutionListener implements ExecutionListener {
    @Autowired
    private FlowMainService flowMainService;
    @Autowired
    private FlowTaskService flowTaskService;

    /**
     * 通用的流程启动处理类，主要用来存储FlowMain和FlowTask数据
     * @param delegateExecution
     */
    public void normalStartEvent(DelegateExecution delegateExecution,String flowPhase,String flowLastPhase) {
        //保存FlowMain
        FlowMain flowMain = new FlowMain();
        String nextUserId = (String) delegateExecution.getVariable("userId");
        if(StringUtils.isNotEmpty(nextUserId)){
            flowMain.setNextUserId(nextUserId);
        }
        String nextUsername = (String) delegateExecution.getVariable("username");
        if(StringUtils.isNotEmpty(nextUsername)){
            flowMain.setNextUsername(nextUsername);
        }
        String currentUserId = (String) delegateExecution.getVariable("currentUserId");
        if(StringUtils.isNotEmpty(currentUserId)){
            flowMain.setFlowUserId(currentUserId);
        }
        String currentUsername = (String) delegateExecution.getVariable("currentUsername");
        if(StringUtils.isNotEmpty(currentUsername)){
            flowMain.setFlowUserName(currentUsername);
        }
        String applyUserId = (String) delegateExecution.getVariable("applyUserId");
        if(StringUtils.isNotEmpty(applyUserId)){
            flowMain.setFlowApplyUserId(applyUserId);
        }
        String applyUsername = (String) delegateExecution.getVariable("applyUsername");
        if(StringUtils.isNotEmpty(applyUsername)){
            flowMain.setFlowApplyUsername(applyUsername);
        }
        String remark = (String) delegateExecution.getVariable("remark");
        if(StringUtils.isNotEmpty(remark)){
            flowMain.setFlowRemark(remark);
        }
        String flowType = (String) delegateExecution.getVariable("flowType");
        if(StringUtils.isNotEmpty(flowType)){
            flowMain.setFlowType(flowType);
        }

        String flowOperation = (String) delegateExecution.getVariable("flowOperation");
        if(StringUtils.isNotEmpty(flowOperation)){
            flowMain.setFlowOperation(flowOperation);
        }

        flowMain.setFlowPhase(flowPhase);
        flowMain.setFlowLastPhase(flowLastPhase);
        flowMain.setFlowMainId("FM"+ UUIDUtil.getUUID());
        flowMain.setFlowExecuteTime(new Date());
        flowMain.setFlowOpinion("流程启动");
        flowMain.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        flowMain.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowMainService.saveFlowMain(flowMain);
        //保存FlowTask
        FlowTask flowTask = new FlowTask();
        flowTask.setFlowTaskId("FT"+ UUIDUtil.getUUID());
        if(StringUtils.isNotEmpty(remark)){
            flowTask.setFlowRemark(remark);
        }
        flowTask.setFlowOpinion("流程启动");
        flowTask.setFlowPhase(flowPhase);
        flowTask.setFlowLastPhase(flowLastPhase);
        flowTask.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowTask.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());

        flowTask.setFlowExecuteTime(new Date());
        if(StringUtils.isNotEmpty(currentUsername)){
            flowTask.setFlowUserName(currentUsername);
        }
        if(StringUtils.isNotEmpty(currentUserId)){
            flowTask.setFlowUserId(currentUserId);
        }
        if(StringUtils.isNotEmpty(nextUserId)){
            flowTask.setNextUserId(nextUserId);
        }
        if(StringUtils.isNotEmpty(nextUsername)){
            flowTask.setNextUsername(nextUsername);
        }
        if(StringUtils.isNotEmpty(flowType)){
            flowTask.setFlowType(flowType);
        }
        if(StringUtils.isNotEmpty(flowOperation)){
            flowTask.setFlowOperation(flowOperation);
        }
        flowTaskService.saveFlowTask(flowTask);
    }

    public void normalAgreeEvent(DelegateExecution delegateExecution,String flowPhase,String flowLastPhase) {
        //更新FlowMain
        FlowMain flowMain = new FlowMain();
        String nextUserId = (String) delegateExecution.getVariable("userId");
        if(StringUtils.isNotEmpty(nextUserId)){
            flowMain.setNextUserId(nextUserId);
        }
        String nextUsername = (String) delegateExecution.getVariable("username");
        if(StringUtils.isNotEmpty(nextUsername)){
            flowMain.setNextUsername(nextUsername);
        }
        String currentUserId = (String) delegateExecution.getVariable("currentUserId");
        if(StringUtils.isNotEmpty(currentUserId)){
            flowMain.setFlowUserId(currentUserId);
        }
        String currentUsername = (String) delegateExecution.getVariable("currentUsername");
        if(StringUtils.isNotEmpty(currentUsername)){
            flowMain.setFlowUserName(currentUsername);
        }
        String flowType = (String) delegateExecution.getVariable("flowType");
        if(StringUtils.isNotEmpty(flowType)){
            flowMain.setFlowType(flowType);
        }
        String applyUserId = (String) delegateExecution.getVariable("applyUserId");
        if(StringUtils.isNotEmpty(applyUserId)){
            flowMain.setFlowApplyUserId(applyUserId);
        }
        String applyUsername = (String) delegateExecution.getVariable("applyUsername");
        if(StringUtils.isNotEmpty(applyUsername)){
            flowMain.setFlowApplyUsername(applyUsername);
        }
        String flowOperation = (String) delegateExecution.getVariable("flowOperation");
        if(StringUtils.isNotEmpty(flowOperation)){
            flowMain.setFlowOperation(flowOperation);
        }
        String remark = (String) delegateExecution.getVariable("remark");
        if(StringUtils.isNotEmpty(remark)){
            flowMain.setFlowRemark(remark);
        }
        flowMain.setFlowPhase(flowPhase);
        flowMain.setFlowLastPhase(flowLastPhase);
        flowMain.setFlowExecuteTime(new Date());
        flowMain.setFlowOpinion("同意");
        flowMain.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        flowMain.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowMainService.updateFlowMain(flowMain);
        //保存FlowTask
        FlowTask flowTask = new FlowTask();
        flowTask.setFlowTaskId("FT"+ UUIDUtil.getUUID());
        if(StringUtils.isNotEmpty(remark)){
            flowTask.setFlowRemark(remark);
        }
        flowTask.setFlowOpinion("同意");
        flowTask.setFlowPhase(flowPhase);
        flowTask.setFlowLastPhase(flowLastPhase);
        flowTask.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowTask.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        flowTask.setFlowExecuteTime(new Date());
        if(StringUtils.isNotEmpty(currentUsername)){
            flowTask.setFlowUserName(currentUsername);
        }
        if(StringUtils.isNotEmpty(currentUserId)){
            flowTask.setFlowUserId(currentUserId);
        }
        if(StringUtils.isNotEmpty(nextUserId)){
            flowTask.setNextUserId(nextUserId);
        }
        if(StringUtils.isNotEmpty(nextUsername)){
            flowTask.setNextUsername(nextUsername);
        }
        if(StringUtils.isNotEmpty(flowOperation)){
            flowTask.setFlowOperation(flowOperation);
        }
        if(StringUtils.isNotEmpty(flowType)){
            flowTask.setFlowType(flowType);
        }
        flowTaskService.saveFlowTask(flowTask);
    }

    public void normalDisAgreeEvent(DelegateExecution delegateExecution,String flowPhase,String flowLastPhase) {
        //更新FlowMain
        FlowMain flowMain = new FlowMain();
        String nextUserId = (String) delegateExecution.getVariable("userId");
        if(StringUtils.isNotEmpty(nextUserId)){
            flowMain.setNextUserId(nextUserId);
        }
        String nextUsername = (String) delegateExecution.getVariable("username");
        if(StringUtils.isNotEmpty(nextUsername)){
            flowMain.setNextUsername(nextUsername);
        }
        String currentUserId = (String) delegateExecution.getVariable("currentUserId");
        if(StringUtils.isNotEmpty(currentUserId)){
            flowMain.setFlowUserId(currentUserId);
        }
        String currentUsername = (String) delegateExecution.getVariable("currentUsername");
        if(StringUtils.isNotEmpty(currentUsername)){
            flowMain.setFlowUserName(currentUsername);
        }
        String applyUserId = (String) delegateExecution.getVariable("applyUserId");
        if(StringUtils.isNotEmpty(applyUserId)){
            flowMain.setFlowApplyUserId(applyUserId);
        }
        String applyUsername = (String) delegateExecution.getVariable("applyUsername");
        if(StringUtils.isNotEmpty(applyUsername)){
            flowMain.setFlowApplyUsername(applyUsername);
        }
        String flowType = (String) delegateExecution.getVariable("flowType");
        if(StringUtils.isNotEmpty(flowType)){
            flowMain.setFlowType(flowType);
        }
        String remark = (String) delegateExecution.getVariable("remark");
        if(StringUtils.isNotEmpty(remark)){
            flowMain.setFlowRemark(remark);
        }
        String flowOperation = (String) delegateExecution.getVariable("flowOperation");
        if(StringUtils.isNotEmpty(flowOperation)){
            flowMain.setFlowOperation(flowOperation);
        }
        flowMain.setFlowPhase(flowPhase);
        flowMain.setFlowLastPhase(flowLastPhase);
        flowMain.setFlowExecuteTime(new Date());
        flowMain.setFlowOpinion("不同意");
        flowMain.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        flowMain.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowMainService.updateFlowMain(flowMain);
        //保存FlowTask
        FlowTask flowTask = new FlowTask();
        flowTask.setFlowTaskId("FT"+ UUIDUtil.getUUID());
        if(StringUtils.isNotEmpty(remark)){
            flowTask.setFlowRemark(remark);
        }
        flowTask.setFlowOpinion("不同意");
        flowTask.setFlowPhase(flowPhase);
        flowTask.setFlowLastPhase(flowLastPhase);
        flowTask.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        flowTask.setProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        flowTask.setFlowExecuteTime(new Date());
        if(StringUtils.isNotEmpty(currentUsername)){
            flowTask.setFlowUserName(currentUsername);
        }
        if(StringUtils.isNotEmpty(currentUserId)){
            flowTask.setFlowUserId(currentUserId);
        }
        if(StringUtils.isNotEmpty(nextUserId)){
            flowTask.setNextUserId(nextUserId);
        }
        if(StringUtils.isNotEmpty(nextUsername)){
            flowTask.setNextUsername(nextUsername);
        }
        if(StringUtils.isNotEmpty(flowOperation)){
            flowTask.setFlowOperation(flowOperation);
        }
        if(StringUtils.isNotEmpty(flowType)){
            flowTask.setFlowType(flowType);
        }
        flowTaskService.saveFlowTask(flowTask);
    }

}
