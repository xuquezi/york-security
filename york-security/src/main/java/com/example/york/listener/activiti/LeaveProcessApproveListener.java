package com.example.york.listener.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class LeaveProcessApproveListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("LeaveProcessApproveListener----------执行----------");
    }
}
