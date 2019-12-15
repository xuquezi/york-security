package com.example.york.service.impl;

import com.example.york.dao.RoleMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.PageInfo;
import com.example.york.entity.Role;
import com.example.york.exception.SelfThrowException;
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

    @Override
    public PageInfo findRolePageInfo(String roleName, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<Role> list = roleMapper.selectRoleList(roleName, start, pageSize);
        Integer total = roleMapper.countRoleList(roleName);
        return new PageInfo(total,list);
    }

    @Override
    public void stopAndUseRole(Integer status, Integer roleId) {
        if(status == 0){
            //传过来状态为0,启用状态，进行停用操作
            //停用角色前，判断有没有用户配置了该角色
            Integer count = userRoleMapper.countUserHaveThisRole(roleId);
            if(count>0){
                // 如果有用户配置了该角色则不能停用，给予提示！
                throw new SelfThrowException("有用户配置了该角色导致无法停用，请先让用户删除该角色！");
            }
            roleMapper.stopRole(roleId);
        }else {
            //传过来状态为1,停用状态，进行启用操作
            roleMapper.useRole(roleId);
        }
    }
}
