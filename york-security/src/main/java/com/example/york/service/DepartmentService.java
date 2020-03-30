package com.example.york.service;

import com.example.york.entity.CodeLib;
import com.example.york.entity.Department;
import com.example.york.entity.DepartmentUserEdit;
import com.example.york.entity.PageInfo;

import java.util.List;

public interface DepartmentService {
    PageInfo queryDepartmentListByPage(String departmentName, Integer limit, Integer page);

    List<Department> queryDepartmentByLevel(String level);

    void createDepartment(Department department);

    Department getDepartmentById(String leaveApplyDepartmentId);

    Department queryDepartmentByManagerId(String userSerial);

    List<Department> queryDepartmentList();

    void updateDepartment(Department department);

    void editDepartmentUser(DepartmentUserEdit departmentUserEdit);

    List<CodeLib> queryDepartmentLevel();

    List<Department> queryDepartmentByParentLevel(String levelString);
}
