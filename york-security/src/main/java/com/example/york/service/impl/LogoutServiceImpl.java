package com.example.york.service.impl;

import com.example.york.dao.LogoutMapper;
import com.example.york.entity.LogoutLog;
import com.example.york.service.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
}
