package com.example.york;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;

public class DeployClass {
    public static void main(String[] str) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        DeploymentBuilder builder =  repositoryService.createDeployment();
        builder.name("LeaveProcess");
        builder.addClasspathResource("processes/LeaveProcess.bpmn20.xml");
        builder.deploy();

    }
}
