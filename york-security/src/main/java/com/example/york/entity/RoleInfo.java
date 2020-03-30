package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class RoleInfo {
    //主键
    private String roleSerial;
    //角色名称
    private String roleName;
    //角色状态
    private Integer status;
    //角色描述
    private String roleDesc;
    //角色创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date roleCreateTime;
    //角色创建用户
    private String roleCreateUser;
    //角色更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date roleUpdateTime;
    //角色更新用户
    private String roleUpdateUser;
    //逻辑删除标志
    private Integer deleteFlag;
}
