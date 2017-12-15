package org.fanye.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static Calendar calendar=Calendar.getInstance();
	
	/**
	 * �õ��������ܼ�
	 * @return
	 */
	public static int getTodayWeek(){
		calendar.setTime(new Date());
		int week=calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(week<0){
			week=7;
		}
		return week;
	}
	
	/**
	 * �õ�ʱ���  ������
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getMinute(Date startDate,Date endDate){
		long start=startDate.getTime();
		long end=endDate.getTime();
		int minute=(int)(end-start)/(1000*60);
		return minute;
	}
	
	/**
	 * ��ȡ����ĳ��ʱ��
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date getDate(int hour,int minute){
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
}
