package com.example.york.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Department {
    //部门id
    private String departmentId;
    //部门name
    private String departmentName;
    // 部门描述
    private String departmentDesc;
    //部门创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    //部门创建用户
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    //部门更新时间
    private Date updateTime;
    //部门更新用户
    private String updateUser;
    //部门是否删除 0没删除，1删除
    private Integer deleteStatus;


}
