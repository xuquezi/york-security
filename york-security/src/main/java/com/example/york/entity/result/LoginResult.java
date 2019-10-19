package com.example.york.entity.result;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult extends ResponseResult {
    //前端要求返回token对象，key+value形式，不能直接返回token字符串。
    private String token;
    public LoginResult(String message, Integer code,String token) {
        super(message, code);
        this.token = token;
    }
}
