package com.example.york.service.impl;

import com.example.york.entity.PageInfo;
import com.example.york.entity.activiti.ProcessTask;
import com.example.york.service.ApplyService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ApplyServiceImpl implements ApplyService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Override
    public void startApplyByKey(String processDefKey) {
        log.info("执行流程启动开始！");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey);
        log.info("流程实例id为： "+ pi.getId());
        log.info("流程定义id为： "+ pi.getProcessDefinitionId());
        log.info("流程定义key为： "+ pi.getProcessDefinitionKey());
        log.info("执行流程启动结束！");
    }

    @Override
    public PageInfo getTaskList(String search, Integer limit, Integer page, Integer currentUserId,String processType) {
        Integer start = (page-1)*limit;
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(currentUserId + "").taskNameLike("%" + search + "%").processDefinitionKey(processType).orderByTaskCreateTime().desc().listPage(start, limit);
        long count = taskService.createTaskQuery().taskAssignee(currentUserId + "").taskNameLike("%" + search + "%").processDefinitionKey(processType).count();
        List<ProcessTask> processTaskList = transferTaskList(taskList);
        return new PageInfo(count,processTaskList);
    }

    // 转化Task到ProcessTask
    private List<ProcessTask> transferTaskList(List<Task> taskList){
        List<ProcessTask> list = new ArrayList<>();
        for (Task task : taskList) {
            ProcessTask processTask = new ProcessTask();
            processTask.setProcessTaskId(task.getId());
            processTask.setProcessTaskCreateTime(task.getCreateTime());
            processTask.setProcessTaskName(task.getName());
            processTask.setProcessTaskInstanceId(task.getProcessInstanceId());
            list.add(processTask);
        }
        return list;
    }
}
