package com.example.york.task;

import com.example.york.annotation.TaskDescribe;
import com.example.york.annotation.TaskLog;
import com.example.york.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
@Slf4j
/**
 * 定时清理SysLog里面的操作日志的任务类
 *
 */
public class ClearSysLogTask {
    @Autowired
    private SysLogService sysLogService;

    /**
     * cron表达式，每天的10点钟执行
     * 执行内容：删除昨天以及之前的操作日志
     */
    @Scheduled(cron = "0 0 10 * * ?")
    //@Scheduled(cron = "*/5 * * * * ?")
    @TaskDescribe("删除昨天以及之前的操作日志")
    @TaskLog
    public void clearSysLogTask() {
        Calendar calendar = Calendar.getInstance();
        //得到昨天的当前时间
        calendar.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String time = yesterday + " 23:59:59";
        //删除昨天以及之前的操作日志
        sysLogService.deleteBeforeTime(time);
        //log.info("执行删除昨天以及之前的操作日志");

    }
}
