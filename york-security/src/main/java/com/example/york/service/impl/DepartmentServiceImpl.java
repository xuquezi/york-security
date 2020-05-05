package com.example.york.service.impl;

import com.example.york.dao.DepartmentMapper;
import com.example.york.entity.*;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.CodeService;
import com.example.york.service.DepartmentService;
import com.example.york.service.RoleService;
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
    @Autowired
    private RoleService roleService;

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
        List<Department> list = departmentMapper.queryDepartmentListByPage(departmentName, start, limit);
        for (Department department : list) {
            //获取部门管理人，部门管理人是部门下有manager角色的用户
            UserInfo userInfo = userService.getDepartmentManagerUserSerial(department.getDepartmentSerial());
            if(userInfo!=null){
                department.setManagerUserSerial(userInfo.getUserSerial());
                department.setManagerUsername(userInfo.getUsername());
            }
        }
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
        // 获取前端传来的部门经理id
        String managerUserSerial = department.getManagerUserSerial();
        // 获取原部门经理id
        UserInfo userInfo = userService.getDepartmentManagerUserSerial(department.getDepartmentSerial());
        // 如果部门没有部门经理
        RoleInfo roleInfo = roleService.queryRoleByRoleName("manager");
        if(roleInfo==null){
            throw new SelfThrowException("没有manager角色或者该角色已经被停用！");
        }
        if(userInfo==null){
            // 直接添加manager角色
            userService.addRoleByUserSerial(roleInfo.getRoleSerial(),managerUserSerial);
        }
        else if(userInfo!=null&&!managerUserSerial.equals(userInfo.getUserSerial())){
            // 如果不相同则部门经理进行了修改。
            // 原部门经理用户manager角色删除
            userService.deleteRoleByUserSerial(roleInfo.getRoleSerial(),userInfo.getUserSerial());
            // 新部门经理用户添加manager角色
            userService.addRoleByUserSerial(roleInfo.getRoleSerial(),managerUserSerial);
        }

        departmentMapper.updateDepartment(department);
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

    /**
     * 校验部门名称
     * @param departmentName
     * @return
     */
    @Override
    public boolean validDepartmentName(String departmentName) {
        List<Department> list = departmentMapper.validDepartmentName(departmentName);
        if(list!=null && list.size()>0){
            return false;
        }
        return true;
    }

    /**
     * 删除部门
     * @param departmentSerial
     */
    @Override
    public void deleteDepartmentByDepartmentSerial(String departmentSerial) {
        //查询部门下用户，包括停用用户，因为停用的可能启用
        List<UserInfo> list1 = userService.departmentUserQuery(departmentSerial);
        if(list1!=null&&list1.size()>0){
            throw new SelfThrowException("部门下还有用户，不能删除！");
        }
        // 获取下级部门
        List<Department> list2 = departmentMapper.queryDepartmentByParentSerial(departmentSerial);
        if(list2!=null&&list2.size()>0){
            throw new SelfThrowException("部门下还有下级部门，不能删除！");
        }
        departmentMapper.deleteDepartmentByDepartmentSerial(departmentSerial);

    }

    /**
     * 更新前校验
     * @param department
     */
    @Override
    public void validateUpdateDepartment(Department department) {
        List<Department> list = departmentMapper.validDepartmentName(department.getDepartmentName());
        for (Department departmentInfo : list) {
            if(!department.getDepartmentSerial().equals(departmentInfo.getDepartmentSerial())){
                throw new SelfThrowException("已有相同的部门名称！");
            }
        }
    }
}
