package com.example.york.listener.leaveprocesslistener;

import com.example.york.constant.ProcessConst;
import com.example.york.listener.FlowExecutionListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;


@Component
public class LeaveProcessStartAgreeListener extends FlowExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        super.normalStartEvent(delegateExecution, ProcessConst.LEAVE_APPLY_START);
    }
}
