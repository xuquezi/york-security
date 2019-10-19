package com.example.york.entity.result;

import com.example.york.entity.ActiveUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResult extends ResponseResult {
    private ActiveUser user;
    public UserResult(String message, Integer code) {
        super(message, code);
    }
}
