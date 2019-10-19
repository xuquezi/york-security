package com.example.york.service.impl;

import com.example.york.dao.RoleMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.Role;
import com.example.york.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        return roleMapper.getRolesByUserId(userId);
    }

    @Override
    public List<Role> getRoles() {
        return roleMapper.getRoles();
    }

    @Override
    public void deleteRolesByUserId(Integer userId) {
        userRoleMapper.deleteRolesByUserId(userId);
    }
}
