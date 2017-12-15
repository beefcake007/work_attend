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
     * 中午十二点 判定上下午
     */
    private static final int NOON_HOUR = 12 ;
    private static final int NOON_MINUTE = 00 ;

    /**
     * 早晚上班时间判定
     */
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    /**
     * 缺勤一整天
     */
    private static final Integer ABSENCE_DAY =480 ;
    /**
     * 考勤异常状态
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
            //查询当天 此人有没有打卡记录
            Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId());
            Date noon = DateUtil.getDate(NOON_HOUR,NOON_MINUTE);
            Date morningAttend = DateUtil.getDate(MORNING_HOUR,MORNING_MINUTE);
            if(todayRecord==null){
                //打卡记录还不存在
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点 上午打卡
                    attend.setAttendMorning(today);
                    //计算打卡时间是不是迟到
                    if(today.compareTo(morningAttend)>0){
                        //大于九点半迟到了
                        attend.setAttendState(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtil.getMinute(morningAttend,today));
                    }

                }else {
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }else{
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点 上午打卡
                    return;
                }else {
                    //晚上打卡
                    todayRecord.setAttendEvening(today);
                    //判断打卡时间是不是18.30以后是不是早退
                    Date eveningAttend = DateUtil.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if(today.compareTo(eveningAttend)<0){
                        //早于下午六点半 早退
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
            log.error("用户签到异常",e);
            throw e;
        }
	}

	@Override
	public PageQueryBean listAttend(QueryCondition condition) {
		//根据条件查询 count 记录数目
		int count=attendMapper.countByCondition(condition);
		PageQueryBean pageResult=new PageQueryBean();
		if(count>0){
			pageResult.setTotalRows(count);
			pageResult.setCurrentPage(condition.getCurrentPage());
			pageResult.setPageSize(condition.getPageSize());
			List<Attend> attendList=attendMapper.selectAttendPage(condition);
			pageResult.setItems(attendList);
		}
		//如果有记录 才去查询分页数据 没有相关记录数目 没必要查分页数据
		return pageResult;
	}

	@Override
	@Transactional
	public void checkAttend() {

        //查询缺勤用户ID 插入打卡记录  并且设置为异常 缺勤480分钟
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
        // 检查晚打卡 将下班未打卡记录设置为异常
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
