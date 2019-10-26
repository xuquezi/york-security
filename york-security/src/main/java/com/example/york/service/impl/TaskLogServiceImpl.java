package com.example.york.service.impl;

import com.example.york.dao.TaskLogMapper;
import com.example.york.entity.TaskSysLog;
import com.example.york.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
