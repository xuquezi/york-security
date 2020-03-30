package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    //主键
    private String userRoleSerial;
    //user_info表的主键
    private String userSerial;
    //role_info表的主键
    private String roleSerial;
}
