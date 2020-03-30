package com.example.york.listener.leaveprocesslistener;

import com.example.york.constant.ProcessConst;
import com.example.york.listener.FlowExecutionListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;


@Component
public class LeaveProcessApplyAgreeListener extends FlowExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        super.normalAgreeEvent(delegateExecution, ProcessConst.LEAVE_PROCESS_APPLY);
    }
}
