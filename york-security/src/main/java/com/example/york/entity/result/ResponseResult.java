package com.example.york.entity.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResponseResult implements Serializable {
    private String message;
    private Integer code;
    public ResponseResult(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
