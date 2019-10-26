package com.example.york.service.impl;

import com.example.york.dao.SysLogMapper;
import com.example.york.entity.PageInfo;
import com.example.york.entity.SysLog;
import com.example.york.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Override
    public void save(SysLog sysLog) {
        sysLogMapper.saveSysLog(sysLog);
    }

    @Override
    public PageInfo findOperateLogList(String username, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<SysLog> list = sysLogMapper.selectOperateLogList(username, start, pageSize);
        Integer total = sysLogMapper.countOperateLogList(username);
        return new PageInfo(total,list);
    }

    @Override
    public void deleteSelected(Integer[] ids) {
        sysLogMapper.deleteSelected(ids);
    }

    @Override
    public void deleteLogById(Integer id) {
        sysLogMapper.deleteLogById(id);
    }

    @Override
    public void deleteBeforeTime(String time) {
        Integer counts = sysLogMapper.deleteBeforeTime(time);

    }

}
