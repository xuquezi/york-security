package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentUserEdit {
    //部门id
    private String departmentSerial;
    //部门下员工的id数组集合
    private String[] departmentUserList;
}
