package com.example.york.service.impl;

import com.example.york.dao.LogoutMapper;
import com.example.york.entity.LogoutLog;
import com.example.york.entity.PageInfo;
import com.example.york.service.LogoutService;
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
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LogoutMapper logoutMapper;

    /**
     * 保存登出日志
     * @param username
     * @param ip
     */
    @Override
    public void saveLogoutLog(String username, String ip) {
        LogoutLog logoutLog = new LogoutLog();
        logoutLog.setLogoutId("LOL"+UUIDUtil.getUUID());
        logoutLog.setLogoutIp(ip);
        logoutLog.setLogoutUsername(username);
        logoutLog.setLogoutTime(new Date());
        logoutMapper.saveLogoutLog(logoutLog);
    }

    /**
     * 分页查询登录日志
     * @param logoutUsername
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo queryLogoutLogByPage(String logoutUsername, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<LogoutLog> list = logoutMapper.queryLogoutLogByPage(logoutUsername, start, pageSize);
        Integer total = logoutMapper.countLogoutLogList(logoutUsername);
        return new PageInfo(total,list);
    }

    /**
     * 删除日志（单条）
     * @param logoutId
     */
    @Override
    public void deleteLogoutLogById(String logoutId) {
        logoutMapper.deleteLogoutLogById(logoutId);
    }

    /**
     * 批量删除日志
     * @param ids
     */
    @Override
    public void deleteSelectedLogoutLog(String[] ids) {
        logoutMapper.deleteSelectedLogoutLog(ids);
    }
}
