package com.example.york.entity.activiti;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessDef {
    private String processDefId;

    private String processDefCategory;

    private String processDefName;

    private String processDefKey;

    private Integer processDefVersion;

    private String processDefDeploymentId;

    private String processDefResourceName;

    private String processDefDiagramResourceName;

    private String processDefEngineVersion;

    private String processDefTenantId;

    private ProcessDeployment processDeployment;


}
