package com.example.york.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProcessTask {

    private String processTaskId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date processTaskCreateTime;

    private String processTaskName;

    private String processTaskInstanceId;

}
