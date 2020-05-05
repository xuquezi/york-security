package com.example.york.service;

import com.example.york.entity.CodeLib;
import com.example.york.entity.Department;
import com.example.york.entity.PageInfo;

import java.util.List;

public interface DepartmentService {
    PageInfo queryDepartmentListByPage(String departmentName, Integer limit, Integer page);

    List<Department> queryDepartmentByLevel(String level);

    void createDepartment(Department department);

    Department getDepartmentById(String leaveApplyDepartmentId);


    List<Department> queryDepartmentList();

    void updateDepartment(Department department);

    List<CodeLib> queryDepartmentLevel();

    List<Department> queryDepartmentByParentLevel(String levelString);

    boolean validDepartmentName(String departmentName);

    void deleteDepartmentByDepartmentSerial(String departmentSerial);

    void validateUpdateDepartment(Department department);
}
