package org.fanye.util;

import org.fanye.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

public class AttendCheckTask {

	@Autowired
	private AttendService attendService;
	
	public void checkAttend(){
		//���Ȼ�ȡ ����û�򿨵��� ��������򿨼�¼   ��������Ϊ�쳣  ȱ��480����
		//����д򿨼�¼  ��������  ���������ǲ�������
		attendService.checkAttend();
	}
}
