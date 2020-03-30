package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.ApproveResult;
import com.example.york.entity.LeaveApply;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.entity.result.LeaveApplyResult;
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

import java.util.Date;

@RestController
@RequestMapping("/apply")
@Slf4j
public class ApplyController {
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private ApplyService applyService;

    @GetMapping("/lastProcessDef/page")
    @ApiOperation(value="分页查询流程定义列表" ,notes="分页查询流程定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "流程定义模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult getLastProcessDefList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = activitiService.getLastProcessDefList(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @GetMapping("/getLeaveWaitApplyList/page")
    @ApiOperation(value="分页查询流程任务列表" ,notes="分页查询流程任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "任务名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "processType", value = "流程类型", required = true, dataType = "String",paramType = "query")
    })
    public PageResult getLeaveWaitApplyList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(name = "processType",defaultValue = "")String processType){
        String currentUserId = getCurrentUserId();
        PageInfo pageInfo = applyService.getLeaveWaitApplyList(search, limit, page,currentUserId,processType);
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

    private String getCurrentUserId(){
        // 获取当前操作用户，作为流程下一阶段的处理人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String userId = userDetail.getUserId();
        return userId;
    }

    @PostMapping("/applyLeaveProcess")
    @SysLog
    @ApiOperation(value="请假申请提交", notes="请假申请提交")
    @ApiImplicitParam(name = "leaveApply", value = "请假申请提交实体", required = true, dataType = "LeaveApply",paramType = "body")
    public ResponseResult applyLeaveProcess(@RequestBody LeaveApply leaveApply) {
        leaveApply.setLeaveApplyTime(new Date());
        applyService.applyLeaveProcess(leaveApply);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/cancelProcess")
    @SysLog
    @ApiOperation(value="根据taskId取消申请", notes="根据taskId取消申请")
    @ApiImplicitParam(name = "processTaskId", value = "taskId", required = true, dataType = "String",paramType = "query")
    public ResponseResult cancelProcess(@RequestParam(value = "processTaskId") String processTaskId){
        applyService.cancelProcess(processTaskId);
        return new ResponseResult("取消申请成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/getApplyingList/page")
    @ApiOperation(value="分页查询审批中列表" ,notes="分页查询审批中列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult getApplyingList(@RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page){
        log.info("当前页为："+ page);
        log.info("每页显示记录数："+ limit);
        String currentUserId = getCurrentUserId();
        PageInfo pageInfo = applyService.getApplyingList(limit, page,currentUserId);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }


    @GetMapping("/getLeaveWaitApproveList/page")
    @ApiOperation(value="分页查询流程任务列表" ,notes="分页查询流程任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "任务名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "processType", value = "流程类型", required = true, dataType = "String",paramType = "query")
    })
    public PageResult getLeaveWaitApproveList(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(name = "processType",defaultValue = "")String processType){
        String currentUserId = getCurrentUserId();
        PageInfo pageInfo = applyService.getLeaveWaitApproveList(search, limit, page,currentUserId,processType);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @GetMapping("/getLeaveApplyData")
    @SysLog
    @ApiOperation(value="获得请假申请数据", notes="获得请假申请数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processTaskInstanceId", value = "流程实例id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "processTaskDefinitionId", value = "流程定义id", required = true, dataType = "String",paramType = "query")
    })

    public LeaveApplyResult getLeaveApplyData(@RequestParam(value = "processTaskInstanceId") String processTaskInstanceId, @RequestParam(value = "processTaskDefinitionId") String processTaskDefinitionId){
        LeaveApply leaveApply = applyService.getLeaveApplyData(processTaskInstanceId,processTaskDefinitionId);
        LeaveApplyResult leaveApplyResult =  new LeaveApplyResult("查询成功",ResponseCode.REQUEST_SUCCESS);
        leaveApplyResult.setLeaveApply(leaveApply);
        return leaveApplyResult;
    }

    @PutMapping("/agreeLeaveApply")
    @SysLog
    @ApiOperation(value="审批同意请假申请", notes="审批同意请假申请")
    @ApiImplicitParam(name = "approveResult", value = "审批同意请假申请", required = true, dataType = "ApproveResult",paramType = "body")
    public ResponseResult agreeLeaveApply(@RequestBody ApproveResult approveResult) {
        applyService.agreeLeaveApply(approveResult);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/backLeaveApply")
    @SysLog
    @ApiOperation(value="审批退回请假申请", notes="审批退回请假申请")
    @ApiImplicitParam(name = "approveResult", value = "审批退回请假申请", required = true, dataType = "ApproveResult",paramType = "body")
    public ResponseResult backLeaveApply(@RequestBody ApproveResult approveResult) {
        applyService.backLeaveApply(approveResult);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

}
