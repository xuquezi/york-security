package com.example.york.dao;

import com.example.york.entity.FlowTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowTaskMapper {
    Integer saveFlowTask(FlowTask flowTask);
}
