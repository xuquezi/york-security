package com.example.york.listener.activiti;

import com.example.york.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class LeaveProcessStartEventListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        log.info("LeaveProcessStartEventListener----------执行开始----------");
        // 获取当前操作用户，作为流程下一阶段的处理人
        Integer currentUserId = getCurrentUserId();
        log.info("当前操作用户的id为： "+currentUserId);
        delegateExecution.setVariable("userId",currentUserId);
        log.info("LeaveProcessStartEventListener----------执行结束----------");

    }

    private Integer getCurrentUserId(){
        // 获取当前操作用户，作为流程下一阶段的处理人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        Integer userId = userDetail.getUserId();
        return userId;
    }

}
