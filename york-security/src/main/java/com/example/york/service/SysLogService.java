package com.example.york.service;

import com.example.york.entity.PageInfo;
import com.example.york.entity.SysLog;

public interface SysLogService {
    void save(SysLog sysLog);

    PageInfo queryOperateLogByPage(String username, Integer pageSize, Integer pageNum);

    void deleteSelectedOperateLog(String[] ids);

    void deleteOperateLog(String id);

    void deleteBeforeTime(String time);

    void deleteAllOperateLog();
}
