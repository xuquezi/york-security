package com.example.york.dao;

import com.example.york.entity.LogoutLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogoutMapper {
    Integer saveLogoutLog(LogoutLog logoutLog);

    List<LogoutLog> selectLogoutLogList(@Param("logoutUsername")String logoutUsername, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countLogoutLogList(@Param("logoutUsername")String logoutUsername);

    Integer deleteLogoutLogById(@Param("logoutId")Integer logoutId);

    Integer deleteSelectedLogoutLog(Integer[] ids);

}
