package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.Const;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.CodeLib;
import com.example.york.entity.Department;
import com.example.york.entity.PageInfo;
import com.example.york.entity.UserInfo;
import com.example.york.entity.result.ListResult;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.DepartmentService;
import com.example.york.service.UserService;
import com.example.york.utils.JwtTokenUtil;
import com.example.york.utils.UUIDUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    @GetMapping("/queryDepartmentList")
    @ApiOperation(value="获取所有部门", notes="获取所有部门")
    public ListResult queryDepartmentList(){
        List<Department> departmentList = departmentService.queryDepartmentList();
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(departmentList);
        return listResult;
    }

    @GetMapping("/queryDepartmentListByPage")
    @ApiOperation(value="分页查询部门信息列表" ,notes="分页查询部门信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "部门名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryDepartmentListByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = departmentService.queryDepartmentListByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @GetMapping("/queryDepartmentLevel")
    @ApiOperation(value="获取所有部门级别", notes="获取所有部门级别")
    public ListResult getRoles(){
        List<CodeLib> codeList = departmentService.queryDepartmentLevel();
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(codeList);
        return listResult;
    }

    @GetMapping("/queryDepartmentByParentLevel")
    @SysLog
    @ApiOperation(value="获取父级部门" ,notes="获取父级部门")
    @ApiImplicitParam(name = "levelString", value = "部门级别", required = true, dataType = "String",paramType = "query")
    public ListResult queryDepartmentByParentLevel(@RequestParam(name = "levelString") String levelString){
        if(StringUtils.isEmpty(levelString)){
            throw new SelfThrowException("获取部门级别失败！");
        }
        List<Department> list = departmentService.queryDepartmentByParentLevel(levelString);
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(list);
        return listResult;
    }

    @PostMapping("/createDepartment")
    @SysLog
    @ApiOperation(value="新增部门", notes="新增部门")
    @ApiImplicitParam(name = "department", value = "部门实体department", required = true, dataType = "Department",paramType = "body")
    public ResponseResult createDepartment(@RequestBody Department department, HttpServletRequest request) {

        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        department.setDepartmentCreateUser(username);//设置创建用户
        department.setDepartmentCreateTime(new Date());//设置创建时间
        department.setDepartmentUpdateTime(new Date());//设置更新时间
        department.setDepartmentUpdateUser(username);//设置更新用户
        department.setDeleteFlag(0);
        department.setDepartmentSerial("DP"+UUIDUtil.getUUID());// 设置uuid主键
        departmentService.createDepartment(department);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/updateDepartment")
    @SysLog
    @ApiOperation(value="修改更新部门", notes="修改更新部门")
    @ApiImplicitParam(name = "department", value = "部门实体", required = true, dataType = "Department",paramType = "body")
    public ResponseResult updateDepartment(@RequestBody Department department,HttpServletRequest request) {
        departmentService.validateUpdateDepartment(department);
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        //设置更新用户
        department.setDepartmentUpdateUser(username);
        //设置更新时间
        department.setDepartmentUpdateTime(new Date());
        departmentService.updateDepartment(department);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/deleteDepartmentByDepartmentSerial")
    @SysLog
    @ApiOperation(value="根据departmentSerial删除部门", notes="根据departmentSerial删除部门")
    @ApiImplicitParam(name = "departmentSerial", value = "角色id", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteDepartmentByDepartmentSerial(@RequestParam(value = "departmentSerial") String departmentSerial){
        departmentService.deleteDepartmentByDepartmentSerial(departmentSerial);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/departmentUserQuery")
    @ApiOperation(value="根据部门id获取部门下员工(包括停用和启用用户)", notes="根据部门id获取部门下员工(包括停用和启用用户)")
    @ApiImplicitParam(name = "departmentSerial", value = "部门id", required = true, dataType = "String",paramType = "query")
    public ListResult departmentUserQuery(@RequestParam(name = "departmentSerial") String departmentSerial){
        List<UserInfo> list = userService.departmentUserQuery(departmentSerial);
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(list);
        return listResult;
    }

}
