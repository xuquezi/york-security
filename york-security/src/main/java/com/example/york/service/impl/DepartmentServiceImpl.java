package com.example.york.service.impl;

import com.example.york.dao.DepartmentMapper;
import com.example.york.entity.*;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.CodeService;
import com.example.york.service.DepartmentService;
import com.example.york.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CodeService codeService;

    /**
     * 分页查询部门列表
     * @param departmentName 部门名称
     * @param limit 每页显示数
     * @param page 当前页
     * @return
     */
    @Override
    public PageInfo queryDepartmentListByPage(String departmentName, Integer limit, Integer page) {
        Integer start = (page-1)*limit;
        List<RoleInfo> list = departmentMapper.queryDepartmentListByPage(departmentName, start, limit);
        Integer total = departmentMapper.countDepartmentList(departmentName);
        return new PageInfo(total,list);
    }

    /**
     * 根据部门级别获取部门列表
     * @param level
     * @return
     */
    @Override
    public List<Department> queryDepartmentByLevel(String level) {
        List<Department> list = departmentMapper.queryDepartmentByLevel(level);
        return list;
    }

    /**
     * 新增部门
     * @param department
     */
    @Override
    public void createDepartment(Department department) {
        departmentMapper.createDepartment(department);
    }

    /**
     * 根据id获取部门
     * @param leaveApplyDepartmentId
     * @return
     */
    @Override
    public Department getDepartmentById(String leaveApplyDepartmentId) {
        return departmentMapper.getDepartmentById(leaveApplyDepartmentId);
    }

    /**
     * 根据部门管理人id获取部门
     * @param userSerial
     * @return
     */
    @Override
    public Department queryDepartmentByManagerId(String userSerial) {
        return departmentMapper.queryDepartmentByManagerId(userSerial);
    }

    /**
     * 查询所有部门列表
     * @return
     */
    @Override
    public List<Department> queryDepartmentList() {
        return departmentMapper.queryDepartmentList();
    }

    /**
     * 更新部门
     * @param department
     */
    @Override
    public void updateDepartment(Department department) {
        departmentMapper.updateDepartment(department);
    }

    /**
     * 编辑部门下的员工
     * @param departmentUserEdit
     */
    @Override
    public void editDepartmentUser(DepartmentUserEdit departmentUserEdit) {
        String departmentSerial = departmentUserEdit.getDepartmentSerial();
        String[] users = departmentUserEdit.getDepartmentUserList();
        List<String> userList= Arrays.asList(users);
        List<String> arrayList=new ArrayList(userList);
        // 获取当前部门的管理人。
        // 如果传过来的数组中不包括当前部门的管理人，则不能修改，部门管理人不能在这里编辑移除。
        String managerUserSerial = queryDepartmentManagerUserSerial(departmentSerial);
        if(StringUtils.isNotEmpty(managerUserSerial)){
            boolean flag = false;
            for (String user : users) {
                if(user.equals(managerUserSerial)){
                    flag = true;
                }
            }
            if(flag == false){
                throw new SelfThrowException("修改失败！部门的管理人不能在这里编辑转移部门！");
            }
        }
        // 排除当前部门的管理人
        arrayList.remove(managerUserSerial);
        //先将部门id是departmentSerial的用户，将外键部门id致空
        userService.resetDepartment(departmentSerial);
        //再设置用户的部门id外键
        userService.updateDepartmentByUserSerial(users,arrayList,departmentSerial);
    }
    private String queryDepartmentManagerUserSerial(String departmentSerial){
        return departmentMapper.queryDepartmentManagerUserSerial(departmentSerial);
    }

    /**
     * 查询配置里面的所有部门级别
     * @return
     */
    @Override
    public List<CodeLib> queryDepartmentLevel() {
        return codeService.queryDepartmentLevel();
    }

    /**
     * 获取父类部门数据列表
     * @param levelString
     * @return
     */
    @Override
    public List<Department> queryDepartmentByParentLevel(String levelString) {
        String parentLevel = codeService.queryParentDepartmentLevel(levelString);
        List<Department> list = queryDepartmentByLevel(parentLevel);
        if(list==null||list.size()<1){
            throw new SelfThrowException("获取父类部门级别失败！请先配置上级部门！");
        }
        return list;
    }
}
