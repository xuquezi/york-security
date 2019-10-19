package com.example.york.service;

import com.example.york.entity.PageInfo;
import com.example.york.entity.SysLog;

public interface SysLogService {
    void save(SysLog sysLog);

    PageInfo findOperateLogList(String username, Integer pageSize, Integer pageNum);

    void deleteSelected(Integer[] ids);

    void deleteLogById(Integer id);

}
