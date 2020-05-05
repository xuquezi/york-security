package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.ApproveResult;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.ApproveService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approve")
@Slf4j
public class ApproveController {
    @Autowired
    private ApproveService approveService;

    @GetMapping("/queryLeaveWaitApproveListByPage/page")
    @ApiOperation(value="分页查询流程任务列表" ,notes="分页查询流程任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "processType", value = "流程类型", required = true, dataType = "String",paramType = "query")
    })
    public PageResult queryLeaveWaitApproveListByPage(@RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam(name = "processType",defaultValue = "")String processType){
        String currentUserId = getCurrentUserId();
        PageInfo pageInfo = approveService.queryLeaveWaitApproveListByPage(limit, page,currentUserId,processType);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    private String getCurrentUserId(){
        // 获取当前操作用户，作为流程下一阶段的处理人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User)authentication.getPrincipal();
        String userId = userDetail.getUserId();
        return userId;
    }

    @PutMapping("/agreeLeaveApply")
    @SysLog
    @ApiOperation(value="审批同意请假申请", notes="审批同意请假申请")
    @ApiImplicitParam(name = "approveResult", value = "审批同意请假申请", required = true, dataType = "ApproveResult",paramType = "body")
    public ResponseResult agreeLeaveApply(@RequestBody ApproveResult approveResult) {
        approveService.agreeLeaveApply(approveResult);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/backLeaveApply")
    @SysLog
    @ApiOperation(value="审批退回请假申请", notes="审批退回请假申请")
    @ApiImplicitParam(name = "approveResult", value = "审批退回请假申请", required = true, dataType = "ApproveResult",paramType = "body")
    public ResponseResult backLeaveApply(@RequestBody ApproveResult approveResult) {
        approveService.backLeaveApply(approveResult);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }
}
