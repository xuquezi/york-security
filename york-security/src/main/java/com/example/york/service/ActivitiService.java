package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface ActivitiService {
    PageInfo getProcessDefList(String search, Integer limit, Integer page);

    void deleteProcessDef(String processDefDeploymentId);

    void deploy(String processDefResourceName,String deployName);

    PageInfo getLastProcessDefList(String search, Integer limit, Integer page);

    void cascadeDeleteProcessDef(String processDefDeploymentId);

}
