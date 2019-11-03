package com.example.york.entity.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooleanResult extends ResponseResult {
    private Boolean flag;

    public BooleanResult(String message, Integer code) {
        super(message, code);
    }
}
