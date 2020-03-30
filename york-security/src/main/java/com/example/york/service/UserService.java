package com.example.york.service;


import com.example.york.entity.PageInfo;
import com.example.york.entity.UserInfo;

import java.util.List;

public interface UserService {
    UserInfo queryUserByUsername(String username);

    PageInfo queryUserListByPage(String username, Integer pageSize, Integer pageNum);

    void updateUser(UserInfo userInfo);

    void stopOrUseUser(Integer status, String userSerial);

    void deleteUserByUserSerial(String userSerial);

    UserInfo queryUserInfoByUserSerial(String userId);

    String queryUsernameByUserSerial(String userSerial);

    void createUser(UserInfo userInfo);

    List<UserInfo> queryAllUserList();

    List<UserInfo> queryUserByDepartmentSerial(String departmentSerial);

    void updateDepartmentByUserSerial(String[] users,List<String> userSerialList, String departmentSerial);

    void resetDepartment(String departmentSerial);

}
