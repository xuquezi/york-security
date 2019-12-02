package com.example.york.service;

import com.example.york.entity.MessageLog;
import com.example.york.entity.PageInfo;

public interface MessageLogService {
    void saveMessageLog(MessageLog messageLog);

    PageInfo findMessageLogList(String mobile, Integer pageSize, Integer pageNum);

    void deleteMessageLog(String messageId);
}
