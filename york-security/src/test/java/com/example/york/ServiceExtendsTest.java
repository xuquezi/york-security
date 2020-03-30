package com.example.york;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceExtendsTest extends YorkApplicationTests {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ManagementService managementService;

    @Test
    public void test(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            String sql = "select e.PROC_INST_ID_,e.START_TIME_ from "+managementService.getTableName(Execution.class)+ " e where e.PROC_DEF_ID_ = #{processDefId} and e.PROC_INST_ID_ = e.ID_";
            String id = processDefinition.getId();
            List<Execution> executionList = runtimeService.createNativeExecutionQuery().sql(sql).parameter("processDefId", id).list();
            for (Execution execution : executionList) {
                System.out.println("id:"+execution.getProcessInstanceId());
            }

        }

    }

}
