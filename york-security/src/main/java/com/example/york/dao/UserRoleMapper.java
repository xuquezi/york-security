package com.example.york.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {
    Integer deleteRolesByUserId(@Param("userId")Integer userId);

    Integer insertRoles(@Param("userId")Integer userId, @Param("roleId")Integer roleId);

    int countUserRoleByUserId(@Param("userId")Integer userId);

    int deleteNotActiveUserRole();

    Integer countUserHaveThisRole(@Param("roleId")Integer roleId);
}
