package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface LogoutService {
    void saveLogoutLog(String username, String ip);

    PageInfo findLogoutLogList(String logoutUsername, Integer pageSize, Integer pageNum);

    void deleteLogoutLogById(Integer logoutId);

    void deleteSelectedLogoutLog(Integer[] ids);

}
