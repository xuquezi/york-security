package com.example.york.service.impl;


import com.example.york.dao.UserMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.Department;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.entity.UserInfo;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.DepartmentService;
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
    private DepartmentService departmentService;
    @Autowired
    private TaskService taskService;

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
     * 获取所有用户
     * @return
     */
    @Override
    public List<UserInfo> queryAllUserList() {
        return userMapper.queryAllUserList();
    }

    /**
     * 根据部门id获取所有部门下的员工列表
     * @param departmentSerial
     * @return
     */
    @Override
    public List<UserInfo> queryUserByDepartmentSerial(String departmentSerial) {
        return userMapper.queryUserByDepartmentSerial(departmentSerial);
    }

    /**
     * 更新修改用户的机构
     * @param
     * @param departmentSerial
     */
    @Override
    public void updateDepartmentByUserSerial(String[] users,List<String> userSerialList, String departmentSerial) {
        for (String userSerial : userSerialList) {
            String username = null;
            Department department = departmentService.queryDepartmentByManagerId(userSerial);
            if(department!=null){
                username = queryUsernameByUserSerial(userSerial);
                throw new SelfThrowException("修改失败！"+username+"是部门管理人，请重新分配部门管理人后转移部门！");
            }
            // 用户是否有在途的流程需要处理，如果有的话需要先处理掉才能停用
            long count = taskService.createTaskQuery()
                    .taskAssignee(userSerial).count();
            if(count>0){
                username = queryUsernameByUserSerial(userSerial);
                throw new SelfThrowException("修改失败！"+username+"还有在途的流程需要处理，不能转移部门！");
            }
        }
        userMapper.updateDepartmentByUserSerial(users,departmentSerial);

    }

    /**
     * 通过部门id，致空用户的部门
     * @param departmentSerial
     */
    @Override
    public void resetDepartment(String departmentSerial) {
        userMapper.resetDepartment(departmentSerial);
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
            Department department = departmentService.queryDepartmentByManagerId(userSerial);
            if(department!=null){
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
         * 停用用户时候判断用户是不是部门管理人
         * 用户是部门管理人需要转移部门管理人后才能停用
         */
        Department department = departmentService.queryDepartmentByManagerId(userSerial);
        if(department!=null){
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
