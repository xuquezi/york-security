package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.Const;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.PageInfo;
import com.example.york.entity.RoleInfo;
import com.example.york.entity.User;
import com.example.york.entity.UserInfo;
import com.example.york.entity.result.*;
import com.example.york.exception.SelfThrowException;
import com.example.york.service.LogoutService;
import com.example.york.service.RoleService;
import com.example.york.service.UserService;
import com.example.york.utils.CommonUtils;
import com.example.york.utils.JwtTokenUtil;
import com.example.york.utils.UUIDUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private LogoutService logoutService;

    @GetMapping("/info")
    @ApiOperation(value="初始化用户信息", notes="初始化用户信息")
    public UserResult getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new SelfThrowException("初始化用户信息失败");
            }
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                User userDetail = (User)authentication.getPrincipal();
                if(userDetail!=null){
                    UserInfo userInfo = userService.queryUserInfoByUserSerial(userDetail.getUserId());
                    // roleList抽取出角色名称存到string数组中，这个是和前端适配的。
                    String[] roles = convertRoleList(userDetail.getRoles());
                    userInfo.setRoles(roles);
                    UserResult userResult = new UserResult("初始化用户信息成功",ResponseCode.REQUEST_SUCCESS);
                    userResult.setUser(userInfo);
                    return userResult;
                }
            }
        }
        throw new SelfThrowException("初始化用户信息失败");
    }

    @GetMapping("/logout")
    @ApiOperation(value="用户退出", notes="用户退出")
    public ResponseResult logout(HttpServletRequest request){

        //记录登出日志start
        String token = request.getHeader(Const.HEADER_STRING);
        String ip = CommonUtils.getIpAddr(request);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if(StringUtils.isNotEmpty(username)){
            //保存登出日志
            logoutService.saveLogoutLog(username,ip);
        }
        //记录登出日志end
        return new ResponseResult("退出成功", ResponseCode.LOGOUT_SUCCESS);
    }

    @GetMapping("/queryUserListByPage")
    @ApiOperation(value="分页查询用户信息列表" ,notes="分页查询用户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "用户名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int",paramType = "query")
    })
    public PageResult queryUserListByPage(@RequestParam(name = "search",defaultValue = "")String search, @RequestParam(name = "limit",defaultValue = "10") Integer limit, @RequestParam(name = "page",defaultValue = "1")Integer page){
        PageInfo pageInfo = userService.queryUserListByPage(search, limit, page);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @PostMapping("/createUser")
    @SysLog
    @ApiOperation(value="新增用户", notes="新增用户")
    @ApiImplicitParam(name = "userInfo", value = "用户实体user", required = true, dataType = "UserInfo",paramType = "body")
    public ResponseResult createUser(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        if(userInfo.getRoles().length < 1){
            //控制用户下必须有权限角色
            throw new SelfThrowException("用户下必须有权限角色");
        }
        if(StringUtils.isEmpty(userInfo.getDepartmentSerial())){
            throw new SelfThrowException("请配置用户部门");
        }

        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);

        userInfo.setUserCreateUser(username);//设置创建用户
        userInfo.setUserCreateTime(new Date());//设置创建时间
        userInfo.setUserUpdateTime(new Date());//设置更新时间
        userInfo.setUserUpdateUser(username);//设置更新用户
        userInfo.setDeleteFlag(0);
        //设置默认头像
        userInfo.setAvatar(Const.DEFAULT_AVATAR_URL);
        //设置默认密码
        userInfo.setPassword(new BCryptPasswordEncoder().encode(Const.DEFAULT_PASSWORD));
        userInfo.setUserSerial("UI"+ UUIDUtil.getUUID());// 设置uuid主键
        userService.createUser(userInfo);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/updateUser")
    @SysLog
    @ApiOperation(value="修改更新用户", notes="修改更新用户")
    @ApiImplicitParam(name = "userInfo", value = "用户实体user", required = true, dataType = "UserInfo",paramType = "body")
    public ResponseResult update(@RequestBody UserInfo userInfo,HttpServletRequest request) {
        if(userInfo.getRoles().length < 1){
            //控制用户下必须有权限角色
            throw new SelfThrowException("用户下必须有权限角色");
        }
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        userInfo.setUserUpdateUser(username);//设置更新用户
        userInfo.setUserUpdateTime(new Date());//设置更新时间
        //配置角色先删除对应角色再重新添加
        roleService.deleteRolesByUserSerial(userInfo.getUserSerial());
        userService.updateUser(userInfo);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @PutMapping("/stopOrUseUser")
    @SysLog
    @ApiOperation(value="根据userId停用或者启用用户", notes="根据userId停用或者启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userSerial", value = "用户Id主键", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "status", value = "用户当前状态", required = true, dataType = "int",paramType = "query")
    })
    public ResponseResult stopOrUseUser(@RequestParam(value = "userSerial") String userSerial,@RequestParam (value = "status")Integer status){
        userService.stopOrUseUser(status,userSerial);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/deleteUserByUserSerial")
    @SysLog
    @ApiOperation(value="根据userId删除用户", notes="根据userId删除用户")
    @ApiImplicitParam(name = "userSerial", value = "用户Id", required = true, dataType = "String",paramType = "query")
    public ResponseResult deleteUserByUserSerial(@RequestParam(value = "userSerial") String userSerial){
        userService.deleteUserByUserSerial(userSerial);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/queryAllUserList")
    @ApiOperation(value="获取所有用户（已启用）", notes="获取所有用户（已启用）")
    public ListResult queryAllUserList(){
        List<UserInfo> userList = userService.queryAllUserList();
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(userList);
        return listResult;
    }

    @GetMapping("/queryUserByDepartmentSerial")
    @ApiOperation(value="根据部门id获取部门下员工", notes="根据部门id获取部门下员工")
    @ApiImplicitParam(name = "departmentSerial", value = "部门id", required = true, dataType = "String",paramType = "query")
    public ListResult queryUserByDepartmentSerial(@RequestParam(name = "departmentSerial") String departmentSerial){
        List<UserInfo> list = userService.queryUserByDepartmentSerial(departmentSerial);
        ListResult listResult = new ListResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        listResult.setList(list);
        return listResult;
    }

    @GetMapping("/queryUserArrayByDepartmentSerial")
    @ApiOperation(value="根据部门id获取部门下员工（返回userId的数组）", notes="根据部门id获取部门下员工（返回userId的数组）")
    @ApiImplicitParam(name = "departmentSerial", value = "部门id", required = true, dataType = "String",paramType = "query")
    public StringArrayResult queryUserArrayByDepartmentSerial(@RequestParam(name = "departmentSerial") String departmentSerial){
        List<UserInfo> list = userService.queryUserByDepartmentSerial(departmentSerial);
        String[] users = convertUserList(list);
        StringArrayResult stringArrayResult = new StringArrayResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        stringArrayResult.setArray(users);
        return stringArrayResult;
    }
    private String[] convertUserList(List<UserInfo> userList) {
        if(userList!=null&&userList.size()>0){
            String[] users = new String[userList.size()];
            for(int i = 0;i<userList.size();i++){
                users[i] = userList.get(i).getUserSerial();
            }
            return users;
        }else {
            return null;
        }
    }

    private String[] convertRoleList(List<RoleInfo> roleList) {
        if(roleList!=null&&roleList.size()>0){
            String[] roles = new String[roleList.size()];
            for(int i = 0;i<roleList.size();i++){
                roles[i] = roleList.get(i).getRoleName();
            }
            return roles;
        }else {
            // 必须有一个以上角色的限制有前端控制!如果前端没有控制这里可以抛出异常控制!
            return null;
        }
    }

}
