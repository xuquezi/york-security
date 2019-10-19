package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.result.PageResult;
import com.example.york.service.SysLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
    @Autowired
    private SysLogService sysLogService;
    @GetMapping("/operate/page")
    @SysLog
    @ApiOperation(value="分页查询操作日志列表" ,notes="分页查询操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "操作用户模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer",paramType = "query")
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
}
