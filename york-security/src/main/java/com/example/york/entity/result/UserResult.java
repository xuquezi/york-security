package com.example.york.entity.result;

import com.example.york.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResult extends ResponseResult {
    private UserInfo user;
    public UserResult(String message, Integer code) {
        super(message, code);
    }
}
