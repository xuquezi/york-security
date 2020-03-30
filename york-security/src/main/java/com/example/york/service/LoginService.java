package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface LoginService {
    void saveLoginLog(String username, String ip);

    PageInfo queryLoginLogByPage(String loginUsername, Integer pageSize, Integer pageNum);

    void deleteLoginLogById(String loginId);

    void deleteSelectedLoginLog(String[] ids);

}
