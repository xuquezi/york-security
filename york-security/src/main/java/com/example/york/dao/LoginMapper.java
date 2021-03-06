package com.example.york.dao;

import com.example.york.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {
    Integer saveLoginLog(LoginLog loginLog);

    List<LoginLog> queryLoginLogByPage(@Param("loginUsername")String loginUsername, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countLoginLogList(@Param("loginUsername")String loginUsername);

    Integer deleteLoginLogById(@Param("loginId")String loginId);

    void deleteSelectedLoginLog(String[] ids);

}
