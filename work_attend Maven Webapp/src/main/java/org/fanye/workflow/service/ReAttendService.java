package org.fanye.workflow.service;

import java.util.List;

import org.fanye.workflow.entity.ReAttend;


public interface ReAttendService {

    void startReAttendFlow(ReAttend reAttend);

    List<ReAttend> listTasks(String userName);

    void approve(ReAttend reAttend);

    List<ReAttend> listReAttend(String username);
}
