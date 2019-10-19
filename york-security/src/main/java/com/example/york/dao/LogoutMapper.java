package com.example.york.dao;

import com.example.york.entity.LogoutLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogoutMapper {
    Integer saveLogoutLog(LogoutLog logoutLog);

}
