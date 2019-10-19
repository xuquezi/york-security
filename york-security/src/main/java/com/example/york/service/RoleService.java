package com.example.york.service;

import com.example.york.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRolesByUserId(Integer userId);

    List<Role> getRoles();

    void deleteRolesByUserId(Integer userId);

}