package com.example.york.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProcessDeployment {

    private String processDeploymentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date processDeploymentTime;

    private String processDeploymentKey;

    private String processDeploymentName;

    private String processDeploymentTenantId;

    private String processDeploymentCategory;


}
