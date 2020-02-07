package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.Role;
import com.example.york.entity.result.ListResult;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.RoleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;
    /**
     * 获取所有角色
     */
    @GetMapping("/list")
    @ApiOperation(value="获取所有角色", notes="获取所有角色")//Swagger2注解
    public ListResult getRoles(){
        List<Role> roleList = roleService.getRoles();
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(roleList);
        return listResult;
    }

    @GetMapping("/page")
    @SysLog
    @ApiOperation(value="分页查询角色信息列表" ,notes="分页查询角色信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "角色名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult getRolePageResult(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        // username，limit，page都设置了默认值，不会为空
        log.info("当前页为："+ page);
        log.info("每页显示记录数："+ limit);
        log.info("搜索名为："+ search);
        PageInfo pageInfo = roleService.findRolePageInfo(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @PutMapping("/stopAndUseRole")
    @SysLog
    @ApiOperation(value="根据roleId停用启用角色", notes="根据roleId停用启用角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "status", value = "更新状态", required = true, dataType = "int",paramType = "query")
    })
    public ResponseResult stopAndUseRole(@RequestParam(value = "roleId") Integer roleId, @RequestParam (value = "status")Integer status){
        roleService.stopAndUseRole(status,roleId);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }
}
