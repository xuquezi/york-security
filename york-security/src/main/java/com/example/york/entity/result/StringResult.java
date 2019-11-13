package com.example.york.entity.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringResult extends ResponseResult {
    private String key;
    public StringResult(String message, Integer code) {
        super(message, code);
    }
}
