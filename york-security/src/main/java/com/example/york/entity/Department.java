package com.example.york.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Department {
    //部门id
    private String departmentSerial;
    //部门name
    private String departmentName;
    // 部门描述
    private String departmentDesc;
    //部门创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date departmentCreateTime;
    //部门创建用户
    private String departmentCreateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //部门更新时间
    private Date departmentUpdateTime;
    //部门更新用户
    private String departmentUpdateUser;
    //部门是否删除 0没删除，1删除
    private Integer deleteFlag;
    // 部门管理人用户id
    private String managerUserSerial;
    // 部门上级部门对应的部门id
    private String parentDepartmentSerial;
    //部门级别
    private String departmentLevel;

    //存放部门管理人的用户信息
    private UserInfo userInfo;



}
