package com.example.york.service.impl;

import com.example.york.entity.PageInfo;
import com.example.york.entity.activiti.ProcessDef;
import com.example.york.entity.activiti.ProcessDeployment;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.ActivitiService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 分页查询流程定义列表
     * @param search
     * @param limit
     * @param page
     * @return
     */
    @Override
    public PageInfo queryProcessDefListByPage(String search, Integer limit, Integer page) {
        Integer start = (page-1)*limit;
        //分页查询
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().processDefinitionNameLike("%"+search+"%").asc().listPage(start,limit);
        //查询总记录数
        long count = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike("%" + search + "%").count();
        // 打印日志
        // processDefLog(processDefinitionList);
        // 需要转化一下，直接转json会报错！
        List<ProcessDef> list = transferProcessDefinitionList(processDefinitionList);
        //获取流程定义对应的部署数据存入ProcessDef内的ProcessDeployment
        list = getProcessDeployment(list);
        PageInfo pageInfo = new PageInfo(count,list);
        return pageInfo;
    }

    /**
     * 根据部署id删除流程定义
     * @param processDefDeploymentId
     */
    @Override
    public void deleteProcessDef(String processDefDeploymentId) {
        try {
            //非级联删除，如果有已经启动的流程实例，就会抛出异常
            repositoryService.deleteDeployment(processDefDeploymentId);
        }catch (Exception e){
            throw new SelfThrowException("该流程定义有已经启动的流程实例！");
        }

    }

    /**
     * 流程定义的部署
     * @param processDefResourceName
     * @param deployName
     */
    @Override
    public void deploy(String processDefResourceName,String deployName) {
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.name(deployName).addClasspathResource(processDefResourceName).deploy();
    }

    /**
     * 分页查询最新的流程定义版本
     * @param search
     * @param limit
     * @param page
     * @return
     */
    @Override
    public PageInfo getLastProcessDefList(String search, Integer limit, Integer page) {
        Integer start = (page-1)*limit;
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().processDefinitionNameLike("%"+search+"%").asc().listPage(start,limit);
        // 过滤最新版本的流程定义，即list中如果两条同一个key 的流程定义，只会取得最新版本的。（分页）
        Map<String ,ProcessDefinition> map = lastVersionFilter(processDefinitionList);
        // map数据存入List<ProcessDef>
        List<ProcessDef> list = mapToProcessDefinitionList(map);

        // 获取所有记录，过滤后获取所有最新版本的流程定义记录，让后获取总记录数
        List<ProcessDefinition> countList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().processDefinitionNameLike("%" + search + "%").asc().list();
        Map countMap = lastVersionFilter(countList);
        Integer count = countMap.size();
        PageInfo pageInfo =new PageInfo(count,list);
        return pageInfo;
    }

    /**
     * 根据流程部署id级联删除流程定义
     * @param processDefDeploymentId
     */
    @Override
    public void cascadeDeleteProcessDef(String processDefDeploymentId) {
        //级联删除，会同时删除已经开启的流程实例的相关数据
        repositoryService.deleteDeployment(processDefDeploymentId,true);
    }

    private List<ProcessDef> mapToProcessDefinitionList(Map<String ,ProcessDefinition> map) {
        List<ProcessDef> list = new ArrayList<>();
        for (ProcessDefinition processDefinition : map.values()) {
            ProcessDef processDef = transferProcessDef(processDefinition);
            list.add(processDef);
        }
        return list;
    }

    //过滤最新版本的流程定义，即list中如果两条同一个key 的流程定义，只会取得最新版本的。
    private Map lastVersionFilter(List<ProcessDefinition> processDefinitionList) {
        Map<String ,ProcessDefinition> map = new LinkedHashMap<String ,ProcessDefinition>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            map.put(processDefinition.getKey(),processDefinition);
        }
        return map;
    }

    //获取流程定义对应的部署数据存入ProcessDef内的ProcessDeployment
    private List<ProcessDef> getProcessDeployment(List<ProcessDef> list) {
        for (ProcessDef processDef : list) {
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(processDef.getProcessDefDeploymentId()).singleResult();
            ProcessDeployment processDeployment = new ProcessDeployment();
            processDeployment.setProcessDeploymentCategory(deployment.getCategory());
            processDeployment.setProcessDeploymentKey(deployment.getKey());
            processDeployment.setProcessDeploymentId(deployment.getId());
            processDeployment.setProcessDeploymentName(deployment.getName());
            processDeployment.setProcessDeploymentTime(deployment.getDeploymentTime());
            processDeployment.setProcessDeploymentTenantId(deployment.getTenantId());
            processDef.setProcessDeployment(processDeployment);
        }
        return list;
    }

    /**
     * 打印日志方法
     * @param processDefinitionList
     */
    private void processDefLog(List<ProcessDefinition> processDefinitionList){
        if(processDefinitionList!=null&&processDefinitionList.size()>0){
            for (ProcessDefinition processDefinition : processDefinitionList) {
                log.info("流程定义id: "+processDefinition.getId());
                log.info("流程定义key: "+processDefinition.getKey());
                log.info("流程定义名: "+processDefinition.getName());
                log.info("流程定义部署id: "+processDefinition.getDeploymentId());
                log.info("流程定义版本: "+processDefinition.getVersion());
            }
        }
    }

    private List<ProcessDef> transferProcessDefinitionList(List<ProcessDefinition> processDefinitionList){
        List<ProcessDef> list = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            ProcessDef processDef = transferProcessDef(processDefinition);
            list.add(processDef);
        }
        return list;
    }

    /**
     * processDefinition转为processDef
     * @param processDefinition
     * @return
     */
    private ProcessDef  transferProcessDef(ProcessDefinition processDefinition){
        ProcessDef processDef = new ProcessDef();
        processDef.setProcessDefCategory(processDefinition.getCategory());
        processDef.setProcessDefDeploymentId(processDefinition.getDeploymentId());
        processDef.setProcessDefId(processDefinition.getId());
        processDef.setProcessDefKey(processDefinition.getKey());
        processDef.setProcessDefName(processDefinition.getName());
        processDef.setProcessDefResourceName(processDefinition.getResourceName());
        processDef.setProcessDefVersion(processDefinition.getVersion());
        processDef.setProcessDefDiagramResourceName(processDefinition.getDiagramResourceName());
        processDef.setProcessDefEngineVersion(processDefinition.getEngineVersion());
        processDef.setProcessDefTenantId(processDefinition.getTenantId());
        processDef.setProcessDefDescription(processDefinition.getDescription());
        return processDef;
    }
}
