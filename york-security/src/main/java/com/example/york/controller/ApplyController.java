package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.ActivitiService;
import com.example.york.service.ApplyService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apply")
@Slf4j
public class ApplyController {
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private ApplyService applyService;

    @GetMapping("/lastProcessDef/page")
    @SysLog
    @ApiOperation(value="分页查询流程定义列表" ,notes="分页查询流程定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "流程定义模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult getLastProcessDefList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page){
        log.info("当前页为："+ page);
        log.info("每页显示记录数："+ limit);
        log.info("搜索名为："+ search);
        PageInfo pageInfo = activitiService.getLastProcessDefList(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    //processType=LeaveProcess
    @GetMapping("/getTaskList/page")
    @SysLog
    @ApiOperation(value="分页查询流程任务列表" ,notes="分页查询流程任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "任务名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "processType", value = "流程类型", required = true, dataType = "String",paramType = "query")
    })
    public PageResult getTaskList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(name = "processType",defaultValue = "")String processType){
        log.info("当前页为："+ page);
        log.info("每页显示记录数："+ limit);
        log.info("搜索名为："+ search);
        log.info("流程类型为："+ processType);
        Integer currentUserId = getCurrentUserId();
        PageInfo pageInfo = applyService.getTaskList(search, limit, page,currentUserId,processType);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @PutMapping("/startApply")
    @SysLog
    @ApiOperation(value="根据流程定义key发起流程", notes="根据流程定义key发起流程")
    @ApiImplicitParam(name = "processDefKey", value = "流程定义key", required = true, dataType = "String",paramType = "query")
    public ResponseResult startApply(@RequestParam(value = "processDefKey") String processDefKey){
        applyService.startApplyByKey(processDefKey);
        return new ResponseResult("发起流程成功",ResponseCode.REQUEST_SUCCESS);
    }

    private Integer getCurrentUserId(){
        // 获取当前操作用户，作为流程下一阶段的处理人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        Integer userId = userDetail.getUserId();
        return userId;
    }
}
