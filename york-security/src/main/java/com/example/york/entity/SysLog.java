package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysLog{
    private String id;
    // 操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date visitTime;
    // 操作人
    private String username;
    // 操作ip
    private String ip;
    // 操作url
    private String url;
    // 耗时（毫秒）
    private Long executionTime;
    // 执行方法
    private String method;
}
