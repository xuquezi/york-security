package com.example.york.dao;

import com.example.york.entity.Department;
import com.example.york.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<RoleInfo> queryDepartmentListByPage(@Param("departmentName")String departmentName, @Param("start")Integer start, @Param("limit")Integer limit);

    Integer countDepartmentList(@Param("departmentName")String departmentName);

    List<Department> queryDepartmentByLevel(@Param("level") String level);

    Integer createDepartment(Department department);

    Department getDepartmentById(@Param("departmentId") String leaveApplyDepartmentId);

    Department queryDepartmentByManagerId(@Param("managerUserId") String userSerial);

    List<Department> queryDepartmentList();

    Integer updateDepartment(Department department);

    String queryDepartmentManagerUserSerial(@Param("departmentSerial") String departmentSerial);
}
