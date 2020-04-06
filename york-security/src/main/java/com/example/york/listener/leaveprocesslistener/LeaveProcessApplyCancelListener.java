package com.example.york.listener.leaveprocesslistener;

import com.example.york.constant.ProcessConst;
import com.example.york.listener.FlowExecutionListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;


@Component
public class LeaveProcessApplyCancelListener extends FlowExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        this.normalDisAgreeEvent(delegateExecution, ProcessConst.LEAVE_PROCESS_CANCEL,ProcessConst.LEAVE_PROCESS_APPLY);
    }
}
