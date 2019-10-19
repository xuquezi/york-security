package com.example.york.dao;

import com.example.york.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> getRolesByUserId(@Param("userId") Integer userId);

    List<Role> getRoles();

}
