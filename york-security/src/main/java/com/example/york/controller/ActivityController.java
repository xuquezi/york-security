package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.ActivitiService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivitiService activitiService;


    @GetMapping("/processDef/page")
    @SysLog
    @ApiOperation(value="分页查询流程定义列表" ,notes="分页查询流程定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "流程定义模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult getProcessDefList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page){
        log.info("当前页为："+ page);
        log.info("每页显示记录数："+ limit);
        log.info("搜索名为："+ search);
        PageInfo pageInfo = activitiService.getProcessDefList(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }


    @DeleteMapping("/processDef/deleteProcessDef")
    @SysLog
    @ApiOperation(value="根据部署id删除流程定义" ,notes="根据部署id删除流程定义")
    @ApiImplicitParam(name = "processDefDeploymentId", value = "部署id", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteProcessDef(@RequestParam(name = "processDefDeploymentId") String processDefDeploymentId){
        activitiService.deleteProcessDef(processDefDeploymentId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/processDef/cascadeDeleteProcessDef")
    @SysLog
    @ApiOperation(value="根据部署id级联删除流程定义" ,notes="根据部署id级联删除流程定义")
    @ApiImplicitParam(name = "processDefDeploymentId", value = "部署id", required = true, dataType = "String",paramType = "query")
    public ResponseResult cascadeDeleteProcessDef(@RequestParam(name = "processDefDeploymentId") String processDefDeploymentId){
        activitiService.cascadeDeleteProcessDef(processDefDeploymentId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/processDef/deploy")
    @SysLog
    @ApiOperation(value="部署流程定义", notes="部署流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefResourceName", value = "部署资源名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "processDeploymentName", value = "部署名", required = true, dataType = "String",paramType = "query")

    })
    public ResponseResult deploy(@RequestParam(name = "processDefResourceName") String processDefResourceName,@RequestParam(name = "processDeploymentName") String processDeploymentName) {
        //部署资源名称: processes/LeaveProcess.bpmn20.xml
        log.info("部署资源名称: "+processDefResourceName);
        //部署名: LeaveProcess
        log.info("部署名: "+processDeploymentName);
        activitiService.deploy(processDefResourceName,processDeploymentName);


        return new ResponseResult("部署成功",ResponseCode.REQUEST_SUCCESS);
    }


}
