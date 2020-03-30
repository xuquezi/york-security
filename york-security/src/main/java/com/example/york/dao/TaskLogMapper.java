package com.example.york.dao;

import com.example.york.entity.TaskSysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskLogMapper {
    Integer saveTaskSysLog(TaskSysLog taskSysLog);

    List<TaskSysLog> queryTaskLogByPage(@Param("taskLogName")String taskLogName, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countTaskLogList(@Param("taskLogName")String taskLogName);

    Integer deleteTaskLog(@Param("taskId") String taskId);

    Integer deleteSelectedTaskLog(String[] ids);
}
