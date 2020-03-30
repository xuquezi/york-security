package com.example.york.service.impl;

import com.example.york.dao.LoginMapper;
import com.example.york.entity.LoginLog;
import com.example.york.entity.PageInfo;
import com.example.york.service.LoginService;
import com.example.york.utils.UUIDUtil;
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
        loginLog.setLoginId("LL"+UUIDUtil.getUUID());
        loginLog.setLoginIp(ip);
        loginLog.setLoginUsername(username);
        loginLog.setLoginTime(new Date());
        loginMapper.saveLoginLog(loginLog);
    }

    /**
     * 分页查询登录日志
     * @param loginUsername
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo queryLoginLogByPage(String loginUsername, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<LoginLog> list = loginMapper.queryLoginLogByPage(loginUsername, start, pageSize);
        Integer total = loginMapper.countLoginLogList(loginUsername);
        return new PageInfo(total,list);
    }

    /**
     * 删除单条登录日志
     * @param loginId
     */
    @Override
    public void deleteLoginLogById(String loginId) {
        loginMapper.deleteLoginLogById(loginId);
    }

    /**
     * 批量删除登录日志
     * @param ids
     */
    @Override
    public void deleteSelectedLoginLog(String[] ids) {
        loginMapper.deleteSelectedLoginLog(ids);
    }
}
