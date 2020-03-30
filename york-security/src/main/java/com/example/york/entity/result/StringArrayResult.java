package com.example.york.entity.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringArrayResult extends ResponseResult{
    private String[] array;
    public StringArrayResult(String message, Integer code) {
        super(message, code);
    }
}
