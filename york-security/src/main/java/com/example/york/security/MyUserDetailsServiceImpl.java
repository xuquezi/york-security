package com.example.york.security;

import com.example.york.entity.RoleInfo;
import com.example.york.entity.User;
import com.example.york.entity.UserInfo;
import com.example.york.service.RoleService;
import com.example.york.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MyUserDetailsServiceImpl：用户名username {}",username);
        UserInfo userInfo = userService.queryUserByUsername(username);
        if(userInfo == null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        List<RoleInfo> roleList = roleService.queryRolesByUserId(userInfo.getUserSerial());
        User user = new User();
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        user.setUserId(userInfo.getUserSerial());
        user.setRoles(roleList);
        return user;
    }
}
