package com.example.york.controller;

import com.example.york.annotation.SysLog;
import com.example.york.constant.Const;
import com.example.york.constant.ResponseCode;
import com.example.york.entity.ActiveUser;
import com.example.york.entity.PageInfo;
import com.example.york.entity.Role;
import com.example.york.entity.User;
import com.example.york.entity.result.PageResult;
import com.example.york.entity.result.ResponseResult;
import com.example.york.entity.result.UserResult;
import com.example.york.service.RoleService;
import com.example.york.service.UserService;
import com.example.york.utils.JwtTokenUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    //用于测试
    @GetMapping("/test")
    public ResponseResult test(){
        return new ResponseResult("test success", ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/info")
    @ApiOperation(value="初始化用户信息", notes="初始化用户信息")//Swagger2注解
    public UserResult getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //有登陆用户就返回登录用户，没有就返回null
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new RuntimeException("初始化用户信息失败");
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                User userDetail = (User)authentication.getPrincipal();
                if(userDetail!=null){
                    if(userDetail.getStatus()==1){
                        throw new RuntimeException("用户已经被停用!请先启用用户!");
                    }
                    ActiveUser activeUser = transferUserToActiveUser(userDetail);
                    UserResult userResult = new UserResult("初始化用户信息成功",ResponseCode.REQUEST_SUCCESS);
                    userResult.setUser(activeUser);
                    return userResult;
                }
            }
        }
        throw new RuntimeException("初始化用户信息失败");
    }

    @GetMapping("/logout")
    @ApiOperation(value="用户退出", notes="用户退出")
    public ResponseResult logout(){
        return new ResponseResult("退出成功", ResponseCode.LOGOUT_SUCCESS);
    }

    @GetMapping("/page")
    @SysLog
    @ApiOperation(value="分页查询用户信息列表" ,notes="分页查询用户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名模糊查询", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示条数", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer",paramType = "query")
    })
    public PageResult getUserPageResult(@RequestParam(name = "search",defaultValue = "")String username, @RequestParam(name = "limit",defaultValue = "10") Integer pageSize, @RequestParam(name = "page",defaultValue = "1")Integer pageNum){
        // username，pageSize，pageNum都设置了默认值，不会为空
        log.info("当前页为："+ pageNum);
        log.info("每页显示记录数："+ pageSize);
        log.info("搜索名为："+ username);
        PageInfo pageInfo = userService.findUserPageInfo(username, pageSize, pageNum);
        PageResult pageResult = new PageResult("查询成功", ResponseCode.REQUEST_SUCCESS);
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    @PutMapping("/update")
    @SysLog
    @ApiOperation(value="修改更新用户", notes="修改更新用户")
    @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "User",paramType = "body")
    public ResponseResult update(@RequestBody User user,HttpServletRequest request) {
        if(user.getRoleArray().length < 1){
            //控制用户下必须有权限角色
            throw new RuntimeException("用户下必须有权限角色");
        }
        String token = request.getHeader(Const.HEADER_STRING);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        user.setUpdateUser(username);//设置更新用户
        user.setUpdateTime(new Date());//设置更新时间
        //配置角色先删除对应角色再重新添加
        roleService.deleteRolesByUserId(user.getUserId());
        userService.updateUser(user);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @GetMapping("/stopAndUse")
    @SysLog
    @ApiOperation(value="根据userId停用启用用户", notes="根据userId停用启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "status", value = "更新状态", required = true, dataType = "Integer",paramType = "query")
    })
    public ResponseResult stopAndUseUser(@RequestParam(value = "userId") Integer userId,@RequestParam (value = "status")Integer status){
        userService.stopAndUseUser(status,userId);
        return new ResponseResult("操作成功",ResponseCode.REQUEST_SUCCESS);
    }

    @DeleteMapping("/deleteUser")
    @SysLog
    @ApiOperation(value="根据userId删除用户", notes="根据userId删除用户")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "Integer",paramType = "query")
    public ResponseResult deleteUser(@RequestParam(value = "userId") Integer userId){
        userService.deleteUser(userId);
        return new ResponseResult("删除成功",ResponseCode.REQUEST_SUCCESS);
    }

    //将user转化为ActiveUser
    private ActiveUser transferUserToActiveUser(User user) {
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setAvatar(user.getAvatar());
        List<Role> roleList = user.getRoles();
        if(roleList!=null&&roleList.size()>0){
            String[] roles = new String[roleList.size()];
            for(int i = 0;i<roleList.size();i++){
                roles[i] = roleList.get(i).getRoleName();
            }
            activeUser.setRoles(roles);
        }else {
            activeUser.setRoles(null);//必须有一个以上角色的限制有前端控制!!如果前端没有控制这里可以抛出异常控制!!
        }
        return activeUser;
    }


}
