package com.example.york.service;

import com.example.york.entity.CodeLib;

import java.util.List;

public interface CodeService {
    List<CodeLib> queryDepartmentLevel();

    String queryParentDepartmentLevel(String levelString);
}
