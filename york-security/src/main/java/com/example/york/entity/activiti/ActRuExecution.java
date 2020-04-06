package com.example.york.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ActRuExecution {

    private String executionId;

    private String procInstId;

    private String parentId;

    private String procDefId;

    private String rootProcInstId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;




}
