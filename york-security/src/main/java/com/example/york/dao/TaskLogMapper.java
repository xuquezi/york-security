package com.example.york.dao;

import com.example.york.entity.TaskSysLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogMapper {
    Integer saveTaskSysLog(TaskSysLog taskSysLog);
}
