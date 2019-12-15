package com.example.york.task;

import com.example.york.annotation.TaskDescribe;
import com.example.york.annotation.TaskLog;
import com.example.york.annotation.TaskName;
import com.example.york.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearNotActiveUserTask {
    @Autowired
    private UserService userService;

    /**
     * cron表达式，每天的9点钟执行
     * 执行内容：删除昨天以及之前的操作日志
     */
    @Scheduled(cron = "0 0 9 * * ?")
    //@Scheduled(cron = "*/5 * * * * ?")
    @TaskDescribe("删除没有激活的注册用户")
    @TaskName("删除没有激活的注册用户")
    @TaskLog
    public void ClearNotActiveUserTask(){
        userService.deleteNotActiveUser();
    }
}
