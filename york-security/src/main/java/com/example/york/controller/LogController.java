package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.LoginService;
import com.example.york.service.SysLogService;
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
            throw new RuntimeException("选择删除的记录数为0");
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
            @ApiImplicitParam(name = "username", value = "登录用户模糊查询", dataType = "String",paramType = "query"),
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

}
