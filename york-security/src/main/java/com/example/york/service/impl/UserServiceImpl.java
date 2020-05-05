package com.example.york.service.impl;


import com.example.york.dao.UserMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.*;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.DepartmentService;
import com.example.york.service.RoleService;
import com.example.york.service.UserService;
import com.example.york.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RoleService roleService;

    /**
     * 通过用户名查找用户 用户名需保持唯一
     * @param username 用户名
     * @return
     */
    @Override
    public UserInfo queryUserByUsername(String username) {
        return userMapper.queryUserByUsername(username);
    }

    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     * @return
     */
    @Override
    public UserInfo queryUserInfoByUserSerial(String userId) {
        return userMapper.queryUserInfoByUserSerial(userId);
    }

    /**
     * 根据userSerial获取用户名
     * @param userSerial
     * @return
     */
    @Override
    public String queryUsernameByUserSerial(String userSerial) {
        return userMapper.queryUsernameByUserSerial(userSerial);
    }

    /**
     * 分页查询用户数据
     * @param username 用户名用于模糊查询
     * @param pageSize 每页显示数
     * @param pageNum 当前页
     * @return
     */
    @Override
    public PageInfo queryUserListByPage(String username, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<User> list = userMapper.queryUserListByPage(username, start, pageSize);
        Integer total = userMapper.countUserList(username);
        return new PageInfo(total,list);
    }

    /**
     * 更新用户
     * @param userInfo 前端用户信息
     */
    @Override
    public void updateUser(UserInfo userInfo) {
        userMapper.updateUser(userInfo);
        for (String roleSerial : userInfo.getRoles()) {
            String urSerial = "UR"+UUIDUtil.getUUID();
            userRoleMapper.insertRoles(userInfo.getUserSerial(),roleSerial,urSerial);
        }
    }

    /**
     * 新建用户
     * @param userInfo
     */
    @Override
    public void createUser(UserInfo userInfo) {
        userMapper.createUser(userInfo);
        for (String roleSerial : userInfo.getRoles()) {
            String urSerial = "UR"+UUIDUtil.getUUID();
            userRoleMapper.insertRoles(userInfo.getUserSerial(),roleSerial,urSerial);
        }
    }

    /**
     * 根据部门id获取所有部门下的员工列表（停用的不包括）
     * @param departmentSerial
     * @return
     */
    @Override
    public List<UserInfo> queryUserByDepartmentSerial(String departmentSerial) {
        return userMapper.queryUserByDepartmentSerial(departmentSerial);
    }

    /**
     * 根据部门id获取所有部门下的员工列表(包括启用和停用)
     * @param departmentSerial
     * @return
     */
    @Override
    public List<UserInfo> departmentUserQuery(String departmentSerial) {
        return userMapper.departmentUserQuery(departmentSerial);
    }


    /**
     * 根据用户名查询是否有同名用户
     * @param username
     * @return
     */
    @Override
    public boolean validUsername(String username) {
        List<UserInfo> list = userMapper.validUsername(username);
        if(list!=null && list.size()>0){
            return false;
        }
        return true;
    }

    /**
     * 校验相同邮箱
     * @param email
     * @return
     */
    @Override
    public boolean validateEmail(String email) {
        List<UserInfo> list = userMapper.validateEmail(email);
        if(list!=null && list.size()>0){
            return false;
        }
        return true;
    }

    /**
     * 校验相同手机号码
     * @param mobile
     * @return
     */
    @Override
    public boolean validateMobile(String mobile) {
        List<UserInfo> list = userMapper.validateMobile(mobile);
        if(list!=null && list.size()>0){
            return false;
        }
        return true;
    }

    /**
     * 更新时候先校验
     * @param userInfo
     */
    @Override
    public void validateUpdateUser(UserInfo userInfo) {
        List<UserInfo> list1 = userMapper.validUsername(userInfo.getUsername());
        for (UserInfo info : list1) {
            if(!userInfo.getUserSerial().equals(info.getUserSerial())){
                throw new SelfThrowException("用户名已经被其他用户占用！");
            }
        }
        List<UserInfo> list2 = userMapper.validateEmail(userInfo.getEmail());
        for (UserInfo info : list2) {
            if(!userInfo.getUserSerial().equals(info.getUserSerial())){
                throw new SelfThrowException("邮箱已经被其他用户占用！");
            }
        }
        List<UserInfo> list3 = userMapper.validateMobile(userInfo.getMobile());
        for (UserInfo info : list3) {
            if(!userInfo.getUserSerial().equals(info.getUserSerial())){
                throw new SelfThrowException("手机号已经被其他用户占用！");
            }
        }

    }

    /**
     * 根据角色id获取用户
     * @param roleSerial
     * @return
     */
    @Override
    public List<UserInfo> queryUserByRole(String roleSerial) {
        return userMapper.queryUserByRole(roleSerial);
    }

    /**
     * 添加角色
     * @param roleSerial
     * @param userSerial
     */
    @Override
    public void addRoleByUserSerial(String roleSerial, String userSerial) {
        String urSerial = "UR"+UUIDUtil.getUUID();
        userRoleMapper.insertRoles(userSerial,roleSerial,urSerial);

    }

    /**
     * 删除角色
     * @param roleSerial
     * @param userSerial
     */
    @Override
    public void deleteRoleByUserSerial(String roleSerial, String userSerial) {
        userRoleMapper.deleteRoleByUserSerialRoleSerial(roleSerial,userSerial);
    }


    /**
     * 获取部门经理
     * @param departmentSerial
     * @return
     */
    @Override
    public UserInfo getDepartmentManagerUserSerial(String departmentSerial) {
        String roleName = "manager";
        return userMapper.getDepartmentManagerUserSerial(departmentSerial,roleName);
    }


    /**
     * 停用或者启用用户
     * @param status 当前用户状态
     * @param userSerial 用户主键id
     */
    @Override
    public void stopOrUseUser(Integer status, String userSerial) {
        if(status == 0){
            /**
             * 停用用户时候判断用户是不是部门管理人
             * 用户是部门管理人需要转移部门管理人后才能停用
             */
            List<RoleInfo> roleList = roleService.queryRolesByUserId(userSerial);
            for (RoleInfo roleInfo : roleList) {
                //这里先写死，后期可以修改配置一下。
                if("manager".equals(roleInfo.getRoleName()))
                    throw new SelfThrowException("该用户是部门管理人，请重新分配部门管理人后停用！");
            }
            // 用户是否有在途的流程需要处理，如果有的话需要先处理掉才能停用
            long count = taskService.createTaskQuery()
                    .taskAssignee(userSerial).count();
            if(count>0){
                throw new SelfThrowException("该用户还有在途的流程需要处理！");
            }
            //传过来状态为0,启用状态，进行停用操作
            userMapper.stopUser(userSerial);

        }else {
            /**
             * 判断用户下角色不为空,为空的话抛出异常。
             * 但是角色停用时应该也会判断是否有用户配置了该角色，有的话应该不让停用。
             */
            int countUserRole = userRoleMapper.countUserRoleByUserSerial(userSerial);
            if(countUserRole <= 0){
                throw new SelfThrowException("该用户下没有角色，请先配置角色权限");
            }
            userMapper.useUser(userSerial);

        }

    }

    /**
     * 根据userSerial删除用户（逻辑删除）
     * @param userSerial
     */
    @Override
    public void deleteUserByUserSerial(String userSerial) {
        /**
         * 删除用户时候判断用户是不是部门管理人
         * 用户是部门管理人需要转移部门管理人后才能停用
         */
        List<RoleInfo> roleList = roleService.queryRolesByUserId(userSerial);
        for (RoleInfo roleInfo : roleList) {
            //这里先写死，后期可以修改配置一下。
            if("manager".equals(roleInfo.getRoleName()))
                throw new SelfThrowException("该用户是部门管理人，请重新分配部门管理人后停用！");
        }
        // 用户是否有在途的流程需要处理，如果有的话需要先处理掉才能停用
        long count = taskService.createTaskQuery()
                .taskAssignee(userSerial).count();
        if(count>0){
            throw new SelfThrowException("该用户还有在途的流程需要处理！");
        }
        //逻辑删除delete状态置为1
        userMapper.deleteUserByUserSerial(userSerial);
        //删除用户之后还要删除用户和角色关联表中的数据
        userRoleMapper.deleteRolesByUserSerial(userSerial);
    }

}
