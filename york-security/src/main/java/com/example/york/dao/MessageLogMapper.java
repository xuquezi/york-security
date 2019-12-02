package com.example.york.dao;

import com.example.york.entity.MessageLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageLogMapper {
    Integer saveMessageLog(MessageLog messageLog);

    List<MessageLog> findMessageLogList(@Param("mobile")String mobile, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countMessageLogList(@Param("mobile")String mobile);

    Integer deleteMessageLog(@Param("messageId")String messageId);

}
