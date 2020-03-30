package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.Const;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.CodeLib;
import com.example.york.entity.Department;
import com.example.york.entity.DepartmentUserEdit;
import com.example.york.entity.PageInfo;
import com.example.york.entity.result.ListResult;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.DepartmentService;
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
        //前端传过来的level格式例如01@00，01表示本机构级别，00代表上级机构的级别
        String level = "";
        if(department.getDepartmentLevel().contains("@")){
            String[] split = department.getDepartmentLevel().split("@");
            level = split[0];
        }else {
            throw new SelfThrowException("获取部门级别失败！");
        }
        // 重新设置部门级别
        department.setDepartmentLevel(level);
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
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        //设置更新用户
        department.setDepartmentUpdateUser(username);
        //设置更新时间
        department.setDepartmentUpdateTime(new Date());
        //配置角色先删除对应角色再重新添加
        departmentService.updateDepartment(department);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/editDepartmentUser")
    @SysLog
    @ApiOperation(value="修改部门下的员工", notes="修改部门下的员工")
    @ApiImplicitParam(name = "departmentUserEdit", value = "部门实体", required = true, dataType = "DepartmentUserEdit",paramType = "body")
    public ResponseResult editDepartmentUser(@RequestBody DepartmentUserEdit departmentUserEdit) {
        departmentService.editDepartmentUser(departmentUserEdit);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }


}
