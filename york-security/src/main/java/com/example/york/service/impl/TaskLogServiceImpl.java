package com.example.york.service.impl;

import com.example.york.dao.TaskLogMapper;
import com.example.york.entity.LogoutLog;
import com.example.york.entity.PageInfo;
import com.example.york.entity.TaskSysLog;
import com.example.york.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@Slf4j
public class TaskLogServiceImpl implements TaskLogService {
    @Autowired
    private TaskLogMapper taskLogMapper;
    @Override
    public void saveTaskSysLog(TaskSysLog taskSysLog) {
        taskLogMapper.saveTaskSysLog(taskSysLog);
    }

    @Override
    public PageInfo findTaskLogList(String taskLogName, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<TaskSysLog> list = taskLogMapper.findTaskLogList(taskLogName, start, pageSize);
        Integer total = taskLogMapper.countTaskLogList(taskLogName);
        return new PageInfo(total,list);
    }

    @Override
    public void deleteTaskLog(Integer taskId) {
        taskLogMapper.deleteTaskLog(taskId);
    }

    @Override
    public void deleteSelectedTaskLog(Integer[] ids) {
        taskLogMapper.deleteSelectedTaskLog(ids);
    }
}
