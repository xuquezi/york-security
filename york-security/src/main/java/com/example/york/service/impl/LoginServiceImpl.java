package com.example.york.service.impl;

import com.example.york.dao.LoginMapper;
import com.example.york.entity.LoginLog;
import com.example.york.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public void saveLoginLog(String username, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(ip);
        loginLog.setLoginUsername(username);
        loginLog.setLoginTime(new Date());
        loginMapper.saveLoginLog(loginLog);
    }
}
