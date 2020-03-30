package com.example.york.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {
    Integer deleteRolesByUserSerial(@Param("userId")String userSerial);

    Integer insertRoles(@Param("userId")String userSerial, @Param("roleId")String roleSerial,@Param("urSerial")String urSerial);

    int countUserRoleByUserSerial(@Param("userId")String userSerial);

    Integer countUserByRoleSerial(@Param("roleId")String roleSerial);

}
