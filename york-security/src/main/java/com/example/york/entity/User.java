package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class User implements UserDetails {
    private Integer userId;
    private String username;
    private String password;
    private String avatar;
    private String email;
    private String tel;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date updateTime;
    private String updateUser;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date born;
    private Integer sex;
    private Integer deleteStatus;
    private List<Role> roles;
    // 用于接受前端传来的角色数组
    private Integer[] roleArray;

    //接受前端验证码
    private String code;
    //接受前端验证码在redis存放的key
    private String key;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add( new SimpleGrantedAuthority( role.getRoleName() ) );
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // 帐户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 帐户是否被冻结
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 帐号是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
