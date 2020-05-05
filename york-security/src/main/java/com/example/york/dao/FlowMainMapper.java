package com.example.york.dao;

import com.example.york.entity.FlowMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowMainMapper {
    Integer saveFlowMain(FlowMain flowMain);

    Integer updateFlowMain(FlowMain flowMain);

    List<FlowMain> queryApplyingListByPage(@Param("flowApplyUserId") String currentUserId, @Param("start")Integer start, @Param("limit") Integer limit);

    Integer countApplyingList(@Param("flowApplyUserId") String currentUserId);

    List<FlowMain> queryCancelApplyListByPage(@Param("flowApplyUserId") String currentUserId, @Param("start")Integer start, @Param("limit") Integer limit);

    Integer countCancelApplyList(@Param("flowApplyUserId") String currentUserId);

    List<FlowMain> queryFinishApplyListByPage(@Param("flowApplyUserId")String currentUserId, @Param("start")Integer start, @Param("limit")Integer limit);

    Integer countFinishApplyList(@Param("flowApplyUserId")String currentUserId);
}
