package com.example.york.dao;

import com.example.york.entity.MessageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageLogMapper {
    Integer saveMessageLog(MessageLog messageLog);

}
