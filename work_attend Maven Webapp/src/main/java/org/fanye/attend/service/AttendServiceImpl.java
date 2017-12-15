package org.fanye.attend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fanye.attend.dao.AttendMapper;
import org.fanye.attend.entity.Attend;
import org.fanye.util.DateUtil;
import org.fanye.util.PageQueryBean;
import org.fanye.vo.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("/attendService")
public class AttendServiceImpl implements AttendService{
	
	/**
     * ����ʮ���� �ж�������
     */
    private static final int NOON_HOUR = 12 ;
    private static final int NOON_MINUTE = 00 ;

    /**
     * �����ϰ�ʱ���ж�
     */
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    /**
     * ȱ��һ����
     */
    private static final Integer ABSENCE_DAY =480 ;
    /**
     * �����쳣״̬
     */
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;

	@Autowired
	private AttendMapper attendMapper;
	
	private Log log=LogFactory.getLog(this.getClass());
	
	//private SimpleDateFormat format=new SimpleDateFormat();
	
	
	@Override
	public void signAttend(Attend attend) {
		try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte)DateUtil.getTodayWeek());
            //��ѯ���� ������û�д򿨼�¼
            Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId());
            Date noon = DateUtil.getDate(NOON_HOUR,NOON_MINUTE);
            Date morningAttend = DateUtil.getDate(MORNING_HOUR,MORNING_MINUTE);
            if(todayRecord==null){
                //�򿨼�¼��������
                if(today.compareTo(noon)<=0){
                    //��ʱ�� ����12�� �����
                    attend.setAttendMorning(today);
                    //�����ʱ���ǲ��ǳٵ�
                    if(today.compareTo(morningAttend)>0){
                        //���ھŵ��ٵ���
                        attend.setAttendState(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtil.getMinute(morningAttend,today));
                    }

                }else {
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }else{
                if(today.compareTo(noon)<=0){
                    //��ʱ�� ����12�� �����
                    return;
                }else {
                    //���ϴ�
                    todayRecord.setAttendEvening(today);
                    //�жϴ�ʱ���ǲ���18.30�Ժ��ǲ�������
                    Date eveningAttend = DateUtil.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if(today.compareTo(eveningAttend)<0){
                        //������������� ����
                        todayRecord.setAttendState(ATTEND_STATUS_ABNORMAL);
                        todayRecord.setAbsence(DateUtil.getMinute(today,eveningAttend));
                    }else {
                        todayRecord.setAttendState(ATTEND_STATUS_NORMAL);
                        todayRecord.setAbsence(0);
                    }
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }

        }catch (Exception e){
            log.error("�û�ǩ���쳣",e);
            throw e;
        }
	}

	@Override
	public PageQueryBean listAttend(QueryCondition condition) {
		//����������ѯ count ��¼��Ŀ
		int count=attendMapper.countByCondition(condition);
		PageQueryBean pageResult=new PageQueryBean();
		if(count>0){
			pageResult.setTotalRows(count);
			pageResult.setCurrentPage(condition.getCurrentPage());
			pageResult.setPageSize(condition.getPageSize());
			List<Attend> attendList=attendMapper.selectAttendPage(condition);
			pageResult.setItems(attendList);
		}
		//����м�¼ ��ȥ��ѯ��ҳ���� û����ؼ�¼��Ŀ û��Ҫ���ҳ����
		return pageResult;
	}

	@Override
	@Transactional
	public void checkAttend() {

        //��ѯȱ���û�ID ����򿨼�¼  ��������Ϊ�쳣 ȱ��480����
        List<Long> userIdList =attendMapper.selectTodayAbsence();
        if(CollectionUtils.isNotEmpty(userIdList)){
            List<Attend> attendList = new ArrayList<>();
            for(Long userId:userIdList){
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte)DateUtil.getTodayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendState(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }
        // ������ ���°�δ�򿨼�¼����Ϊ�쳣
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if(CollectionUtils.isNotEmpty(absenceList)){
            for(Attend attend : absenceList){
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendState(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }
		
	}

}
