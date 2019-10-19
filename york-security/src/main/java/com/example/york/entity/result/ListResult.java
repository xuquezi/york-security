package com.example.york.entity.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult extends ResponseResult {
    private List list;
    public ListResult(String message, Integer code) {
        super(message, code);
    }
}
