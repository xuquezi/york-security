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
    @Autowired
    private MessageLogService messageLogService;

    @GetMapping("/operate/page")
    @SysLog
    @ApiOperation(value="分页查询操作日志列表" ,notes="分页查询操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "操作用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult findOperateLogList(@RequestParam(name = "search",defaultValue = "")String username, @RequestParam(value = "limit",defaultValue = "10") Integer pageSize, @RequestParam(value = "page",defaultValue = "1")Integer pageNum){
        log.info("当前页为："+ pageNum);
        log.info("每页显示记录数："+ pageSize);
        log.info("搜索名为："+ username);
        PageInfo pageInfo = sysLogService.findOperateLogList(username, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }
    @DeleteMapping("/operate/deleteSelected")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "Integer[]",paramType = "body")
    public ResponseResult deleteSelected(@RequestBody Integer[] ids){
        /*for (Integer id : ids) {
            log.info("要删除的记录 {}",id);
        }*/
        if(ids.length>0){
            sysLogService.deleteSelected(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @DeleteMapping("/operate/deleteLog")
    @SysLog
    @ApiOperation(value="删除单条记录" ,notes="删除单条记录")
    @ApiImplicitParam(name = "id", value = "要删除的记录的主键", required = true, dataType = "int",paramType = "query")
    public ResponseResult deleteLog(@RequestParam(name = "id") Integer id){
        sysLogService.deleteLogById(id);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/login/page")
    @SysLog
    @ApiOperation(value="分页查询登录日志信息列表" ,notes="分页查询登录日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUsername", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult findLoginLogList(@RequestParam(name = "search",defaultValue = "")String loginUsername, @RequestParam(name = "limit",defaultValue = "10") Integer pageSize, @RequestParam(name = "page",defaultValue = "1")Integer pageNum){
        log.info("当前页为："+ pageNum);
        log.info("每页显示记录数："+ pageSize);
        log.info("搜索名为："+ loginUsername);
        PageInfo pageInfo = loginService.findLoginLogList(loginUsername, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }


    @GetMapping("/logout/page")
    @SysLog
    @ApiOperation(value="分页查询登出日志信息列表" ,notes="分页查询登出日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logoutUsername", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult findLogoutLogList(@RequestParam(name = "search",defaultValue = "")String logoutUsername, @RequestParam(name = "limit",defaultValue = "10") Integer pageSize, @RequestParam(name = "page",defaultValue = "1")Integer pageNum){
        PageInfo pageInfo = logoutService.findLogoutLogList(logoutUsername, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }


    @DeleteMapping("/login/deleteLoginLog")
    @SysLog
    @ApiOperation(value="删除单条登录日志记录" ,notes="删除单条登录日志记录")
    @ApiImplicitParam(name = "loginId", value = "要删除的记录的主键", required = true, dataType = "int",paramType = "query")
    public ResponseResult deleteLoginLog(@RequestParam(name = "loginId") Integer loginId){
        loginService.deleteLoginLogById(loginId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/logout/deleteLogoutLog")
    @SysLog
    @ApiOperation(value="删除单条登出日志记录" ,notes="删除单条登出日志记录")
    @ApiImplicitParam(name = "logoutId", value = "要删除的记录的主键", required = true, dataType = "int",paramType = "query")
    public ResponseResult deleteLogoutLog(@RequestParam(name = "logoutId") Integer logoutId){
        logoutService.deleteLogoutLogById(logoutId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/login/deleteSelectedLoginLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "Integer[]",paramType = "body")
    public ResponseResult deleteSelectedLoginLog(@RequestBody Integer[] ids){
        /*for (Integer id : ids) {
            log.info("要删除的记录 {}",id);
        }*/
        if(ids.length>0){
            loginService.deleteSelectedLoginLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @GetMapping("/task/page")
    @SysLog
    @ApiOperation(value="分页查询任务日志信息列表" ,notes="分页查询任务日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskLogName", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult findTaskLogList(@RequestParam(name = "search",defaultValue = "")String taskLogName, @RequestParam(name = "limit",defaultValue = "10") Integer pageSize, @RequestParam(name = "page",defaultValue = "1")Integer pageNum){
        PageInfo pageInfo = taskLogService.findTaskLogList(taskLogName, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @DeleteMapping("/task/deleteTaskLog")
    @SysLog
    @ApiOperation(value="删除单条任务日志记录" ,notes="删除单条任务日志记录")
    @ApiImplicitParam(name = "taskId", value = "要删除的记录的主键", required = true, dataType = "int",paramType = "query")
    public ResponseResult deleteTaskLog(@RequestParam(name = "taskId") Integer taskId){
        taskLogService.deleteTaskLog(taskId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/message/page")
    @SysLog
    @ApiOperation(value="分页查询message日志信息列表" ,notes="分页查询message日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "发送号码模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult findMessageLogList(@RequestParam(name = "search",defaultValue = "")String mobile, @RequestParam(name = "limit",defaultValue = "10") Integer pageSize, @RequestParam(name = "page",defaultValue = "1")Integer pageNum){
        PageInfo pageInfo = messageLogService.findMessageLogList(mobile, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @DeleteMapping("/message/deleteMessageLog")
    @SysLog
    @ApiOperation(value="删除单条Message日志记录" ,notes="删除单条Message日志记录")
    @ApiImplicitParam(name = "messageId", value = "要删除的记录的主键", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteMessageLog(@RequestParam(name = "messageId") String messageId){
        messageLogService.deleteMessageLog(messageId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }


    @DeleteMapping("/logout/deleteSelectedLogoutLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "Integer[]",paramType = "body")
    public ResponseResult deleteSelectedLogoutLog(@RequestBody Integer[] ids){
        /*for (Integer id : ids) {
            log.info("要删除的记录 {}",id);
        }*/
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
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "Integer[]",paramType = "body")
    public ResponseResult deleteSelectedTaskLog(@RequestBody Integer[] ids){
        /*for (Integer id : ids) {
            log.info("要删除的记录 {}",id);
        }*/
        if(ids.length>0){
            taskLogService.deleteSelectedTaskLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

    @DeleteMapping("/message/deleteSelectedMessageLog")
    @SysLog
    @ApiOperation(value="删除选择的记录" ,notes="批量删除选择的记录")
    @ApiImplicitParam(name = "ids", value = "选择的主键数组", required = true, dataType = "String[]",paramType = "body")
    public ResponseResult deleteSelectedMessageLog(@RequestBody String[] ids){
        /*for (Integer id : ids) {
            log.info("要删除的记录 {}",id);
        }*/
        if(ids.length>0){
            messageLogService.deleteSelectedMessageLog(ids);
            return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
        }else {
            throw new SelfThrowException("选择删除的记录数为0");
        }
    }

}
