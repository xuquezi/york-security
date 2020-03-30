package com.example.york.service.impl;

import com.example.york.dao.RoleMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.PageInfo;
import com.example.york.entity.RoleInfo;
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

    /**
     * 根据用户id获取用户的所有角色
     * @param userSerial
     * @return
     */
    @Override
    public List<RoleInfo> queryRolesByUserId(String userSerial) {
        return roleMapper.queryRolesByUserId(userSerial);
    }

    /**
     * 获取所有角色列表
     * @return
     */
    @Override
    public List<RoleInfo> queryRoleList() {
        return roleMapper.queryRoleList();
    }

    /**
     * 根据userId删除角色
     * @param userSerial
     */
    @Override
    public void deleteRolesByUserSerial(String userSerial) {
        userRoleMapper.deleteRolesByUserSerial(userSerial);
    }

    /**
     * 分页查询角色列表
     * @param roleName 角色名用于模糊查询
     * @param pageSize 每页显示数
     * @param pageNum 当前页
     * @return
     */
    @Override
    public PageInfo queryRoleListByPage(String roleName, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<RoleInfo> list = roleMapper.queryRoleListByPage(roleName, start, pageSize);
        Integer total = roleMapper.countRoleList(roleName);
        return new PageInfo(total,list);
    }

    /**
     * 根据roleSerial启用/停用角色
     * @param status
     * @param roleSerial 角色id
     */
    @Override
    public void stopOrUseRole(Integer status, String roleSerial) {
        if(status == 0){
            // 传过来状态为0,启用状态，进行停用操作
            // 停用角色前，判断有没有用户配置了该角色
            Integer count = userRoleMapper.countUserByRoleSerial(roleSerial);
            if(count>0){
                // 如果有用户配置了该角色则不能停用，给予提示
                throw new SelfThrowException("有用户配置了该角色导致无法停用，请先让用户删除该角色！");
            }
            roleMapper.stopRole(roleSerial);
        }else {
            // 传过来状态为1,停用状态，进行启用操作
            roleMapper.useRole(roleSerial);
        }
    }

    /**
     *根据roleSerial删除角色
     * @param roleSerial
     */
    @Override
    public void deleteRoleByRoleSerial(String roleSerial) {
        Integer count = userRoleMapper.countUserByRoleSerial(roleSerial);
        if(count>0){
            // 如果有用户配置了该角色则不能停用，给予提示。
            throw new SelfThrowException("有用户配置了该角色导致无法停用，请先让用户删除该角色！");
        }
        roleMapper.deleteRoleByRoleSerial(roleSerial);
    }

    /**
     * 更新角色
     * @param roleInfo
     */
    @Override
    public void updateRole(RoleInfo roleInfo) {
        roleMapper.updateRole(roleInfo);
    }

    /**
     * 新增角色
     * @param roleInfo
     */
    @Override
    public void createRole(RoleInfo roleInfo) {
        roleMapper.createRole(roleInfo);
    }
}
