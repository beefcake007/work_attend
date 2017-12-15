package org.fanye.attend.service;

import org.fanye.attend.entity.Attend;
import org.fanye.util.PageQueryBean;
import org.fanye.vo.QueryCondition;

public interface AttendService {

	void signAttend(Attend attend);

	PageQueryBean listAttend(QueryCondition condition);

	void checkAttend();
}
