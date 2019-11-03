package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageLog {
    //主键
    private String messageId;
    //短信内容
    private String content;
    //发送号码
    private String mobile;
    //发送时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date sendDate;
    //是否发送成功 0成功,1等待消费,2失败
    private Integer sendFlag;
}
