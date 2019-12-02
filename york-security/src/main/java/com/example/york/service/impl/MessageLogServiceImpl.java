package com.example.york.service.impl;

import com.example.york.dao.MessageLogMapper;
import com.example.york.entity.MessageLog;
import com.example.york.entity.PageInfo;
import com.example.york.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class MessageLogServiceImpl implements MessageLogService {
    @Autowired
    private MessageLogMapper messageLogMapper;

    @Override
    public void saveMessageLog(MessageLog messageLog) {
        messageLogMapper.saveMessageLog(messageLog);
    }

    @Override
    public PageInfo findMessageLogList(String mobile, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<MessageLog> list = messageLogMapper.findMessageLogList(mobile, start, pageSize);
        Integer total = messageLogMapper.countMessageLogList(mobile);
        return new PageInfo(total,list);
    }

    @Override
    public void deleteMessageLog(String messageId) {
        messageLogMapper.deleteMessageLog(messageId);
    }
}
