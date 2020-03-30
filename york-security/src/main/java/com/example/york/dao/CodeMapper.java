package com.example.york.dao;

import com.example.york.entity.CodeLib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeMapper {
    List<CodeLib> queryDepartmentLevel(@Param("codeType") String codeType);

    String queryParentDepartmentLevel(@Param("codeNo") String levelString, @Param("codeType")String codeType);
}
