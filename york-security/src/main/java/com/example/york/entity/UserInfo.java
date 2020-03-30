package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class UserInfo {
    //主键id
    private String userSerial;
    //用户名
    private String username;
    //密码
    private String password;
    //头像url
    private String avatar;
    //邮箱
    private String email;
    //手机号码
    private String mobile;
    //状态
    private Integer status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date userCreateTime;
    //创建用户
    private String userCreateUser;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date userUpdateTime;
    //更新用户
    private String userUpdateUser;
    //出生日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date born;
    //性别
    private Integer sex;
    //逻辑删除标志
    private Integer deleteFlag;

    private Department department;

    private List<RoleInfo> roleList;

    private String[] roles;
    //接受前端传过来的信息
    private String departmentSerial;
}
