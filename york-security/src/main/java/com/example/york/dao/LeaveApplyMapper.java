package com.example.york.dao;

import com.example.york.entity.LeaveApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LeaveApplyMapper {
    Integer saveLeaveApply(LeaveApply leaveApply);

    LeaveApply getLeaveApplyData(@Param("processTaskInstanceId") String processTaskInstanceId,@Param("processTaskDefinitionId") String processTaskDefinitionId);

    Integer updateLeaveApply(LeaveApply leaveApply);

}
