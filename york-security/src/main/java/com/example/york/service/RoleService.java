package com.example.york.service;

import com.example.york.entity.PageInfo;
import com.example.york.entity.RoleInfo;

import java.util.List;

public interface RoleService {

    List<RoleInfo> queryRolesByUserId(String userSerial);

    List<RoleInfo> queryRoleList();

    void deleteRolesByUserSerial(String userSerial);

    PageInfo queryRoleListByPage(String roleName, Integer pageSize, Integer pageNum);

    void stopOrUseRole(Integer status, String roleSerial);

    void deleteRoleByRoleSerial(String roleSerial);

    void updateRole(RoleInfo roleInfo);

    void createRole(RoleInfo roleInfo);

}
