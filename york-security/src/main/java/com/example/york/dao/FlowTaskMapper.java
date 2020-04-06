package com.example.york.dao;

import com.example.york.entity.FlowTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowTaskMapper {
    Integer saveFlowTask(FlowTask flowTask);

    List<FlowTask> queryProcess(@Param("processDefinitionId") String processDefinitionId, @Param("processInstanceId")String processInstanceId);

}
