package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Role {
    private Integer roleId;
    private String roleName;
    private Integer status;
    private String desc;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date updateTime;
    private String updateUser;
    private Integer deleteStatus;
}
