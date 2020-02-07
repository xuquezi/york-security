package com.example.york.listener.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

@Slf4j
public class LeaveProcessEndEventListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        log.info("LeaveProcessEndEventListener----------执行----------");
    }
}
