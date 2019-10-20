package com.example.york.service.impl;

import com.example.york.dao.LoginMapper;
import com.example.york.entity.LoginLog;
import com.example.york.entity.PageInfo;
import com.example.york.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Override
    public PageInfo findLoginLogList(String loginUsername, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<LoginLog> list = loginMapper.selectLoginLogList(loginUsername, start, pageSize);
        Integer total = loginMapper.countLoginLogList(loginUsername);
        return new PageInfo(total,list);
    }

    @Override
    public void deleteLoginLogById(Integer loginId) {
        loginMapper.deleteLoginLogById(loginId);
    }
}
