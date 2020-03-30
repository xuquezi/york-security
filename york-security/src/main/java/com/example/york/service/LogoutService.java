package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface LogoutService {
    void saveLogoutLog(String username, String ip);

    PageInfo queryLogoutLogByPage(String logoutUsername, Integer pageSize, Integer pageNum);

    void deleteLogoutLogById(String logoutId);

    void deleteSelectedLogoutLog(String[] ids);

}
