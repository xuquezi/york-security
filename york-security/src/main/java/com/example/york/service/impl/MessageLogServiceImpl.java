package com.example.york.service.impl;

import com.example.york.dao.MessageLogMapper;
import com.example.york.entity.MessageLog;
import com.example.york.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
