package com.example.york.service.impl;


import com.example.york.dao.UserMapper;
import com.example.york.dao.UserRoleMapper;
import com.example.york.entity.PageInfo;
import com.example.york.entity.User;
import com.example.york.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    /**
     * 通过用户名查找用户 用户名需保持唯一
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        //注册时控制用户名唯一，所以这里直接返回list第一条数据
        return userMapper.findByUsername(username).get(0);
    }

    /**
     * 分页查询用户数据
     * @param username
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findUserPageInfo(String username, Integer pageSize, Integer pageNum) {
        Integer start = (pageNum-1)*pageSize;
        List<User> list = userMapper.selectUserList(username, start, pageSize);
        //log.info(list.size()+"");
        Integer total = userMapper.countUserList(username);
        return new PageInfo(total,list);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
        for (Integer roleId : user.getRoleArray()) {
            userRoleMapper.insertRoles(user.getUserId(),roleId);
        }
    }

    @Override
    public void stopAndUseUser(Integer status, Integer userId) {
        if(status == 0){
            //传过来状态为0,启用状态，进行停用操作
            userMapper.stopUser(userId,status);
        }else {
            //判断用户下角色不为空,为空的话抛出异常，保险起见，但是
            //角色停用时应该也会判断是否有用户配置了该角色，有的话应该不让停用。
            int countUserRole = userRoleMapper.countUserRoleByUserId(userId);
            if(countUserRole <= 0){
                throw new RuntimeException("该用户下没有角色，请先配置角色权限");
            }
            userMapper.useUser(userId,status);

        }

    }

    @Override
    public void deleteUser(Integer userId) {
        //逻辑删除delete状态置为1
        userMapper.deleteUser(userId);
        //删除用户之后还要删除用户和角色关联表中的数据
        userRoleMapper.deleteRolesByUserId(userId);
    }

}