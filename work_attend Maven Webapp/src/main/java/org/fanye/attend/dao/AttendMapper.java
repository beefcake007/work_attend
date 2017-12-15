package org.fanye.attend.dao;

import java.util.List;

import org.fanye.attend.entity.Attend;
import org.fanye.vo.QueryCondition;

public interface AttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

	Attend selectTodaySignRecord(Long userId);

	int countByCondition(QueryCondition condition);

	List<Attend> selectAttendPage(QueryCondition condition);

	List<Long> selectTodayAbsence();

	List<Attend> selectTodayEveningAbsence();

	void batchInsert(List<Attend> attendList);
}