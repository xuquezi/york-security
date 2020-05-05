package com.example.york.controller;

import com.example.york.constant.ResponseCode;
import com.example.york.entity.result.BooleanResult;
import com.example.york.service.DepartmentService;
import com.example.york.service.RoleService;
import com.example.york.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/valid")
@Slf4j
public class ValidController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/validateUsername")
    @ApiOperation(value="校验是否有同名的用户", notes="校验是否有同名的用户")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",paramType = "query")
    public BooleanResult validUsername(@RequestParam(value = "username") String username){
        boolean flag = userService.validUsername(username);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        booleanResult.setFlag(flag);
        return booleanResult;
    }

    @GetMapping("/validateEmail")
    @ApiOperation(value="校验是否相同的邮箱", notes="校验是否相同的邮箱")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String",paramType = "query")
    public BooleanResult validateEmail(@RequestParam(value = "email") String email){
        boolean flag = userService.validateEmail(email);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        booleanResult.setFlag(flag);
        return booleanResult;
    }

    @GetMapping("/validateMobile")
    @ApiOperation(value="校验是否相同的手机", notes="校验是否相同的手机")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String",paramType = "query")
    public BooleanResult validateMobile(@RequestParam(value = "mobile") String mobile){
        boolean flag = userService.validateMobile(mobile);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        booleanResult.setFlag(flag);
        return booleanResult;
    }

    @GetMapping("/validateRoleName")
    @ApiOperation(value="校验是否相同的角色名", notes="校验是否相同的角色名")
    @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String",paramType = "query")
    public BooleanResult validateRoleName(@RequestParam(value = "roleName") String roleName){
        boolean flag = roleService.validateRoleName(roleName);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        booleanResult.setFlag(flag);
        return booleanResult;
    }

    @GetMapping("/validDepartmentName")
    @ApiOperation(value="校验是否相同的部门名称", notes="校验是否相同的部门名称")
    @ApiImplicitParam(name = "departmentName", value = "部门名称", required = true, dataType = "String",paramType = "query")
    public BooleanResult validDepartmentName(@RequestParam(value = "departmentName") String departmentName){
        boolean flag = departmentService.validDepartmentName(departmentName);
        BooleanResult booleanResult = new BooleanResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        booleanResult.setFlag(flag);
        return booleanResult;
    }
}
