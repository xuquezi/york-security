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

    /**
     * 保存定时任务执行日志
     * @param taskSysLog
     */
    @Override
    public void saveTaskSysLog(TaskSysLog taskSysLog) {
        taskLogMapper.saveTaskSysLog(taskSysLog);
    }

    /**
     * 分页查询定时任务执行日志
     * @param taskLogName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo queryTaskLogByPage(String taskLogName, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<TaskSysLog> list = taskLogMapper.queryTaskLogByPage(taskLogName, start, pageSize);
        Integer total = taskLogMapper.countTaskLogList(taskLogName);
        return new PageInfo(total,list);
    }

    /**
     * 删除定时任日志（单条）
     * @param taskId
     */
    @Override
    public void deleteTaskLog(String taskId) {
        taskLogMapper.deleteTaskLog(taskId);
    }

    /**
     * 批量删除定时任务日志
     * @param ids
     */
    @Override
    public void deleteSelectedTaskLog(String[] ids) {
        taskLogMapper.deleteSelectedTaskLog(ids);
    }
}
