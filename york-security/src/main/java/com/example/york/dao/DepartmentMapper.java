package com.example.york.dao;

import com.example.york.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> queryDepartmentListByPage(@Param("departmentName")String departmentName, @Param("start")Integer start, @Param("limit")Integer limit);

    Integer countDepartmentList(@Param("departmentName")String departmentName);

    List<Department> queryDepartmentByLevel(@Param("level") String level);

    Integer createDepartment(Department department);

    Department getDepartmentById(@Param("departmentId") String leaveApplyDepartmentId);


    List<Department> queryDepartmentList();

    Integer updateDepartment(Department department);

    List<Department> validDepartmentName(@Param("departmentName")String departmentName);

    void deleteDepartmentByDepartmentSerial(@Param("departmentId")String departmentSerial);

    List<Department> queryDepartmentByParentSerial(@Param("parentDepartmentSerial")String departmentSerial);

}
