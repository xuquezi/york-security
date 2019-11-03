package com.example.york.service;


import com.example.york.entity.PageInfo;
import com.example.york.entity.User;

public interface UserService {
    User findByUsername(String username);

    PageInfo findUserPageInfo(String username, Integer pageSize, Integer pageNum);

    void updateUser(User user);

    void stopAndUseUser(Integer status, Integer userId);

    void deleteUser(Integer userId);

    Boolean validateUsername(String username);

    Boolean validateEmail(String email);

    void activateUser(Integer userId);

}
