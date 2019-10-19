package com.example.york.controller;

import com.example.york.constant.ResponseCode;
import com.example.york.entity.Role;
import com.example.york.entity.result.ListResult;
import com.example.york.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
