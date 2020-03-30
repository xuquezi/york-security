package com.example.york.entity.result;

import com.example.york.entity.LeaveApply;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveApplyResult extends ResponseResult {
    private LeaveApply leaveApply;
    public LeaveApplyResult(String message, Integer code) {
        super(message, code);
    }
}
