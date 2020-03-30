package com.example.york.dao;

import com.example.york.entity.ActRuExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActRuExecutionMapper {
    List<ActRuExecution> getRuntimeProcess(@Param("procDefId") String id);

}
