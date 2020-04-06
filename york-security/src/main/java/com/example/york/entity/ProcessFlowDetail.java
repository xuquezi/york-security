package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ProcessFlowDetail {
    //标题
    private String title;
    //内容
    private String content;

    private Date timestamp;
}
