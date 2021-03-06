package com.example.york.dao;

import com.example.york.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysLogMapper {
    Integer saveSysLog(SysLog sysLog);

    List<SysLog> queryOperateLogByPage(@Param("username") String username, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countOperateLogList(@Param("username")String username);

    Integer deleteSelectedOperateLog(String[] ids);

    Integer deleteOperateLog(@Param("id")String id);

    Integer deleteBeforeTime(@Param("time") String time);

    Integer deleteAllOperateLog();


}
