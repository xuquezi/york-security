package com.example.york.dao;

import com.example.york.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    Integer saveLoginLog(LoginLog loginLog);
}
