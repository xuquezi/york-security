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

    List<UserInfo> queryUserByDepartmentSerial(String departmentSerial);

    boolean validUsername(String username);

    boolean validateEmail(String email);

    UserInfo getDepartmentManagerUserSerial(String departmentSerial);

    boolean validateMobile(String mobile);

    void validateUpdateUser(UserInfo userInfo);

    List<UserInfo> queryUserByRole(String roleSerial);

    void addRoleByUserSerial(String roleSerial, String userSerial);

    void deleteRoleByUserSerial(String roleSerial, String userSerial);

    List<UserInfo> departmentUserQuery(String departmentSerial);
}
