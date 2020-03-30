package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private TaskLogService taskLogService;

    @GetMapping("/operate/queryOperateLogByPage")
    @ApiOperation(value="分页查询操作日志列表" ,notes="分页查询操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "操作用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryOperateLogByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(value = "limit",defaultValue = "10") Integer limit, @RequestParam(value = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = sysLogService.queryOperateLogByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }
    @DeleteMapping("/operate/deleteSelectedOperateLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "String[]",paramType = "body")
    public ResponseResult deleteSelectedOperateLog(@RequestBody String[] ids){
        if(ids.length>0){
            sysLogService.deleteSelectedOperateLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @DeleteMapping("/operate/deleteOperateLog")
    @SysLog
    @ApiOperation(value="删除单条记录" ,notes="删除单条记录")
    @ApiImplicitParam(name = "id", value = "要删除的记录的主键", required = true, dataType = "int",paramType = "query")
    public ResponseResult deleteOperateLog(@RequestParam(name = "id") String id){
        sysLogService.deleteOperateLog(id);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/operate/deleteAllOperateLog")
    @SysLog
    @ApiOperation(value="删除所有操作记录" ,notes="删除所有操作记录")
    public ResponseResult deleteAllOperateLog(){
        sysLogService.deleteAllOperateLog();
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/login/queryLoginLogByPage")
    @ApiOperation(value="分页查询登录日志信息列表" ,notes="分页查询登录日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryLoginLogByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = loginService.queryLoginLogByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @GetMapping("/logout/queryLogoutLogByPage")
    @ApiOperation(value="分页查询登出日志信息列表" ,notes="分页查询登出日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryLogoutLogByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = logoutService.queryLogoutLogByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }


    @DeleteMapping("/login/deleteLoginLog")
    @SysLog
    @ApiOperation(value="删除单条登录日志记录" ,notes="删除单条登录日志记录")
    @ApiImplicitParam(name = "loginId", value = "要删除的记录的主键", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteLoginLog(@RequestParam(name = "loginId") String loginId){
        loginService.deleteLoginLogById(loginId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/logout/deleteLogoutLog")
    @SysLog
    @ApiOperation(value="删除单条登出日志记录" ,notes="删除单条登出日志记录")
    @ApiImplicitParam(name = "logoutId", value = "要删除的记录的主键", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteLogoutLog(@RequestParam(name = "logoutId") String logoutId){
        logoutService.deleteLogoutLogById(logoutId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/login/deleteSelectedLoginLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "String[]",paramType = "body")
    public ResponseResult deleteSelectedLoginLog(@RequestBody String[] ids){
        if(ids.length>0){
            loginService.deleteSelectedLoginLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @GetMapping("/task/queryTaskLogByPage")
    @ApiOperation(value="分页查询任务日志信息列表" ,notes="分页查询任务日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryTaskLogByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = taskLogService.queryTaskLogByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @DeleteMapping("/task/deleteTaskLog")
    @SysLog
    @ApiOperation(value="删除单条任务日志记录" ,notes="删除单条任务日志记录")
    @ApiImplicitParam(name = "taskId", value = "要删除的记录的主键", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteTaskLog(@RequestParam(name = "taskId") String taskId){
        taskLogService.deleteTaskLog(taskId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }


    @DeleteMapping("/logout/deleteSelectedLogoutLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "String[]",paramType = "body")
    public ResponseResult deleteSelectedLogoutLog(@RequestBody String[] ids){
        if(ids.length>0){
            logoutService.deleteSelectedLogoutLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @DeleteMapping("/task/deleteSelectedTaskLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "String[]",paramType = "body")
    public ResponseResult deleteSelectedTaskLog(@RequestBody String[] ids){
        if(ids.length>0){
            taskLogService.deleteSelectedTaskLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

}
