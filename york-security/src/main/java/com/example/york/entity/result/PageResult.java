package com.example.york.entity.result;

import com.example.york.entity.PageInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResult extends ResponseResult{
    private PageInfo pageInfo;
    public PageResult(String message, Integer code) {
        super(message, code);
    }
}
