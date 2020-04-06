package com.example.york.task;

import com.example.york.annotation.TaskDescribe;
import com.example.york.annotation.TaskLog;
import com.example.york.annotation.TaskName;
import com.example.york.dao.activiti.ActRuExecutionMapper;
import com.example.york.entity.activiti.ActRuExecution;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 定时清理超时流程
 * 流程指定日期内没结束审批，自动终止流程
 *
 */
public class ApplyOverTimeTask {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ActRuExecutionMapper actRuExecutionMapper;

    /**
     * cron表达式，每天的9点钟执行
     * 执行内容：定时清理超时请假流程
     * 如果请假流程30日内没结束审批，自动终止流程
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @TaskDescribe("流程指定日期内没结束审批，自动终止流程")
    @TaskName("定时清理超时流程")
    @TaskLog
    public void applyOverTimeTask() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            //如果是请假流程
            if(processDefinition.getName().equals("LeaveProcess")){
                leaveApplyOverTimeTask(processDefinition);
            }

        }

    }

    private void leaveApplyOverTimeTask(ProcessDefinition processDefinition) {
        String id = processDefinition.getId();

        List<ActRuExecution> list = actRuExecutionMapper.getRuntimeProcess(id);
        for (ActRuExecution actRuExecution : list) {

        }

    }
}
