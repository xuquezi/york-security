package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.Const;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.RoleInfo;
import com.example.york.entity.result.ListResult;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.service.RoleService;
import com.example.york.utils.JwtTokenUtil;
import com.example.york.utils.UUIDUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/queryRoleList")
    @ApiOperation(value="获取所有角色", notes="获取所有角色")
    public ListResult queryRoleList(){
        List<RoleInfo> roleList = roleService.queryRoleList();
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(roleList);
        return listResult;
    }

    @GetMapping("/queryRoleListByPage")
    @ApiOperation(value="分页查询角色信息列表" ,notes="分页查询角色信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "角色名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryRoleListByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = roleService.queryRoleListByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @PutMapping("/stopOrUseRole")
    @SysLog
    @ApiOperation(value="根据roleSerial停用/启用角色", notes="根据roleSerial停用/启用角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleSerial", value = "角色Id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "status", value = "更新状态", required = true, dataType = "int",paramType = "query")
    })
    public ResponseResult stopOrUseRole(@RequestParam(value = "roleSerial") String roleSerial, @RequestParam (value = "status")Integer status){
        roleService.stopOrUseRole(status,roleSerial);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/deleteRoleByRoleSerial")
    @SysLog
    @ApiOperation(value="根据roleSerial删除用户", notes="根据roleSerial删除用户")
    @ApiImplicitParam(name = "roleSerial", value = "角色id", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteRoleByRoleSerial(@RequestParam(value = "roleSerial") String roleSerial){
        roleService.deleteRoleByRoleSerial(roleSerial);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/updateRole")
    @SysLog
    @ApiOperation(value="修改更新角色", notes="修改更新角色")
    @ApiImplicitParam(name = "roleInfo", value = "角色实体role", required = true, dataType = "RoleInfo",paramType = "body")
    public ResponseResult updateRole(@RequestBody RoleInfo roleInfo,HttpServletRequest request) {
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        roleInfo.setRoleUpdateUser(username);//设置更新用户
        roleInfo.setRoleUpdateTime(new Date());//设置更新时间
        roleService.updateRole(roleInfo);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PostMapping("/createRole")
    @SysLog
    @ApiOperation(value="新增角色", notes="新增角色")
    @ApiImplicitParam(name = "roleInfo", value = "角色实体role", required = true, dataType = "RoleInfo",paramType = "body")
    public ResponseResult createRole(@RequestBody RoleInfo roleInfo, HttpServletRequest request) {
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);

        roleInfo.setRoleCreateUser(username);//设置创建用户
        roleInfo.setRoleCreateTime(new Date());//设置创建时间
        roleInfo.setRoleUpdateTime(new Date());//设置更新时间
        roleInfo.setRoleUpdateUser(username);//设置更新用户
        roleInfo.setDeleteFlag(0);
        roleInfo.setRoleSerial("RI"+ UUIDUtil.getUUID());
        roleService.createRole(roleInfo);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }
}
