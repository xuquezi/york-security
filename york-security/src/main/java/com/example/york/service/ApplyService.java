package com.example.york.service;

import com.example.york.entity.PageInfo;

public interface ApplyService {
    void startApplyByKey(String processDefKey);

    PageInfo getTaskList(String search, Integer limit, Integer page, Integer currentUserId,String processType);

}
