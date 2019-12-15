package com.example.york.dao;

import com.example.york.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> getRolesByUserId(@Param("userId") Integer userId);

    List<Role> getRoles();

    List<Role> selectRoleList(@Param("roleName")String roleName, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    Integer countRoleList(@Param("roleName")String roleName);

    Integer stopRole(@Param("roleId")Integer roleId);

    Integer useRole(@Param("roleId")Integer roleId);

}
