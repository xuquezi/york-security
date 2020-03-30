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

    /**
     * 分页查询操作日志
     * @param username
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo queryOperateLogByPage(String username, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<SysLog> list = sysLogMapper.queryOperateLogByPage(username, start, pageSize);
        Integer total = sysLogMapper.countOperateLogList(username);
        return new PageInfo(total,list);
    }

    /**
     * 批量删除操作日志
     * @param ids
     */
    @Override
    public void deleteSelectedOperateLog(String[] ids) {
        sysLogMapper.deleteSelectedOperateLog(ids);
    }

    /**
     * 删除单条操作日志
     * @param id
     */
    @Override
    public void deleteOperateLog(String id) {
        sysLogMapper.deleteOperateLog(id);
    }

    /**
     * 删除特定时间点之前的操作日志
     * @param time
     */
    @Override
    public void deleteBeforeTime(String time) {
        Integer counts = sysLogMapper.deleteBeforeTime(time);
    }

    /**
     * 删除所有操作日志
     */
    @Override
    public void deleteAllOperateLog() {
        sysLogMapper.deleteAllOperateLog();
    }

}
