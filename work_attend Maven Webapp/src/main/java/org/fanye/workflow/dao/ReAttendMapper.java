package org.fanye.workflow.dao;

import java.util.List;

import org.fanye.workflow.entity.ReAttend;

public interface ReAttendMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(ReAttend record);

    int insertSelective(ReAttend record);

    ReAttend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReAttend record);

    int updateByPrimaryKey(ReAttend record);

    List<ReAttend> selectReAttendRecord(String username);
}