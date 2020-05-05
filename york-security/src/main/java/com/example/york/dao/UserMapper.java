package com.example.york.dao;

import com.example.york.entity.User;
import com.example.york.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    UserInfo queryUserByUsername(@Param("username") String username);

    List<User> queryUserListByPage(@Param("username") String username, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countUserList(@Param("username")String username);

    Integer updateUser(UserInfo userInfo);

    Integer stopUser(@Param("userId")String userSerial);

    Integer useUser(@Param("userId")String userSerial);

    Integer deleteUserByUserSerial(@Param("userId")String userSerial);

    UserInfo queryUserInfoByUserSerial(@Param("userId")String userId);

    String queryUsernameByUserSerial(@Param("userId")String userSerial);

    void createUser(UserInfo userInfo);

    List<UserInfo> queryAllUserList();

    List<UserInfo> queryUserByDepartmentSerial(@Param("departmentSerial") String departmentSerial);

    List<UserInfo> departmentUserQuery(@Param("departmentSerial")String departmentSerial);

    List<UserInfo> validUsername(@Param("username")String username);

    List<UserInfo> validateEmail(@Param("email") String email);

    UserInfo getDepartmentManagerUserSerial(@Param("departmentSerial") String departmentSerial,@Param("roleName") String roleName);

    List<UserInfo> validateMobile(@Param("mobile") String mobile);

    List<UserInfo> queryUserByRole(@Param("roleSerial")String roleSerial);

}
