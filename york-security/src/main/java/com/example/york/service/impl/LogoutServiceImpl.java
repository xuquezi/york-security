package com.example.york.service.impl;

import com.example.york.dao.LogoutMapper;
import com.example.york.entity.LogoutLog;
import com.example.york.entity.PageInfo;
import com.example.york.service.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LogoutMapper logoutMapper;
    @Override
    public void saveLogoutLog(String username, String ip) {
        LogoutLog logoutLog = new LogoutLog();
        logoutLog.setLogoutIp(ip);
        logoutLog.setLogoutUsername(username);
        logoutLog.setLogoutTime(new Date());
        logoutMapper.saveLogoutLog(logoutLog);
    }

    @Override
    public PageInfo findLogoutLogList(String logoutUsername, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<LogoutLog> list = logoutMapper.selectLogoutLogList(logoutUsername, start, pageSize);
        Integer total = logoutMapper.countLogoutLogList(logoutUsername);
        return new PageInfo(total,list);
    }
}
