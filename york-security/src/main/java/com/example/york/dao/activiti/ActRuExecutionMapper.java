package com.example.york.dao.activiti;

import com.example.york.entity.activiti.ActRuExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActRuExecutionMapper {
    List<ActRuExecution> getRuntimeProcess(@Param("procDefId") String id);

}
