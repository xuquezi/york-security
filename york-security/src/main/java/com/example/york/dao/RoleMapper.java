package com.example.york.dao;

import com.example.york.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<RoleInfo> queryRolesByUserId(@Param("userId") String userSerial);

    List<RoleInfo> queryRoleList();

    List<RoleInfo> queryRoleListByPage(@Param("roleName")String roleName, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countRoleList(@Param("roleName")String roleName);

    Integer stopRole(@Param("roleId")String roleSerial);

    Integer useRole(@Param("roleId")String roleSerial);

    Integer deleteRoleByRoleSerial(@Param("roleId")String roleSerial);

    Integer updateRole(RoleInfo roleInfo);

    Integer createRole(RoleInfo roleInfo);

}
