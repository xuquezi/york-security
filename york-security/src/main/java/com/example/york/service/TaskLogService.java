package com.example.york.service;

import com.example.york.entity.PageInfo;
import com.example.york.entity.TaskSysLog;

public interface TaskLogService {
    void saveTaskSysLog(TaskSysLog taskSysLog);

    PageInfo queryTaskLogByPage(String taskLogName, Integer pageSize, Integer pageNum);

    void deleteTaskLog(String taskId);

    void deleteSelectedTaskLog(String[] ids);

}
