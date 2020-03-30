package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApproveResult {

    private String approveOpinion;

    private String approveRemark;

    private String processTaskId;

    private String applyUserId;

    private String applyUsername;
}
