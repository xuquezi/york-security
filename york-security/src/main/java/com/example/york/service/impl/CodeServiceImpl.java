package com.example.york.service.impl;

import com.example.york.dao.CodeMapper;
import com.example.york.entity.CodeLib;
import com.example.york.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeMapper codeMapper;

    /**
     * 获取部门级别配置
     * @return
     */
    @Override
    public List<CodeLib> queryDepartmentLevel() {
        String codeType = "DepartmentLevel";
        List<CodeLib> list = codeMapper.queryDepartmentLevel(codeType);
        return list;
    }

    /**
     * 获取父类部门级别
     * @param levelString
     * @return
     */
    @Override
    public String queryParentDepartmentLevel(String levelString) {
        String codeType = "DepartmentLevel";
        return codeMapper.queryParentDepartmentLevel(levelString,codeType);
    }
}
