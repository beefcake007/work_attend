package org.fanye.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.fanye.attend.dao.AttendMapper;
import org.fanye.attend.entity.Attend;
import org.fanye.workflow.dao.ReAttendMapper;
import org.fanye.workflow.entity.ReAttend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ��ǩʵ����
 * @author fanye
 *
 */
@Service
public class ReAttendServiceImpl implements ReAttendService{


    private static final java.lang.String RE_ATTEND_FLOW_ID = "re_attend";

    /**
     * ��ǩ����״̬
     */
    private static final Byte RE_ATTEND_STATUS_ONGOING =1 ;
    private static final Byte RE_ATTEND_STATUS_PSSS =2 ;
    private static final Byte RE_ATTEND_STATUS_REFUSE =3 ;
    /**
     *
     */
    private static final Byte ATTEND_STATUS_NORMAL =1 ;
    /**
     * ������һ��������
     */
    private static final String NEXT_HANDLER = "next_handler";
    /**
     * ���������ǩ���ݼ�
     */
    private static final String RE_ATTEND_SIGN = "re_attend";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReAttendMapper reAttendMapper;

    @Autowired
    private AttendMapper attendMapper;

    /**
     * ������ǩ������
     */
    @Override
    @Transactional
    public void startReAttendFlow(ReAttend reAttend){
        //�ӹ�˾��֯�ܹ��� ��ѯ�������ϼ��쵼�û���
        reAttend.setCurrentHandler("���¾��ޡ�����");
        reAttend.setStatus(RE_ATTEND_STATUS_ONGOING);
        //�������ݿⲹǩ��
        reAttendMapper.insertSelective(reAttend);
        Map<String,Object> map = new HashMap<>();
        map.put(RE_ATTEND_SIGN,reAttend);
        map.put(NEXT_HANDLER,reAttend.getCurrentHandler());
       //������ǩ����ʵ��
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(RE_ATTEND_FLOW_ID,map);
        //�ύ�û���ǩ����
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.complete(task.getId(),map);
    }
    
    /**
     * ���������������  ��ѯĳ����Ҫ���������
     */
    @Override
    public List<ReAttend> listTasks(String userName) {

        List<ReAttend> reAttendList = new ArrayList<>();
        List<Task> taskList= taskService.createTaskQuery().processVariableValueEquals(userName).list();
        //ת����ҳ��ʵ��
        if(CollectionUtils.isNotEmpty(taskList)){
            for(Task task : taskList){
                Map<String, Object> variable = taskService.getVariables(task.getId());
                ReAttend reAttend = (ReAttend)variable.get(RE_ATTEND_SIGN);
                reAttend.setTaskId(task.getId());
                reAttendList.add(reAttend);
            }
        }
        return reAttendList;
    }
    
    /**
     * ����������
     */
    @Override
    @Transactional
    public void approve(ReAttend reAttend) {
       Task task =  taskService.createTaskQuery().taskId(reAttend.getTaskId()).singleResult();

       if((""+RE_ATTEND_STATUS_PSSS).equals(reAttend.getApproveFlag())){
           //����ͨ�� �޸Ĳ�ǩ����״̬
           //�޸���ؿ������� ����״̬��Ϊ����
           Attend attend = new Attend();
           attend.setId(reAttend.getAttendId());
           attend.setAttendState(ATTEND_STATUS_NORMAL);
           attendMapper.updateByPrimaryKeySelective(attend);
           reAttend.setStatus(RE_ATTEND_STATUS_PSSS);
           reAttendMapper.updateByPrimaryKeySelective(reAttend);
       }else if((""+RE_ATTEND_STATUS_REFUSE).equals(reAttend.getApproveFlag())){
           reAttend.setStatus(RE_ATTEND_STATUS_REFUSE);
           reAttendMapper.updateByPrimaryKeySelective(reAttend);
       }
       taskService.complete(reAttend.getTaskId());
    }
    
    /**
     * ��ѯ��ǩ����״̬
     */
    @Override
    public List<ReAttend> listReAttend(String username) {
        List<ReAttend> list =reAttendMapper.selectReAttendRecord(username);
        return list;
    }
}
