package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface LoginService {
    void saveLoginLog(String username, String ip);

    PageInfo findLoginLogList(String loginUsername, Integer pageSize, Integer pageNum);

    void deleteLoginLogById(Integer loginId);

    void deleteSelectedLoginLog(Integer[] ids);

}
