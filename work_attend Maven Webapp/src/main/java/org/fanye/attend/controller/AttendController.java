package org.fanye.attend.controller;

import javax.servlet.http.HttpSession;

import org.fanye.attend.entity.Attend;
import org.fanye.attend.service.AttendService;
import org.fanye.user.entity.User;
import org.fanye.util.PageQueryBean;
import org.fanye.vo.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/attend")
public class AttendController {

	@Autowired
	private AttendService attendService;
	
	@RequestMapping
	public String toAttend(){
		return "attend";
	}
	
	@RequestMapping("/sign")
	@ResponseBody
	public String signAttend(@RequestBody Attend attend)throws Exception{
		attendService.signAttend(attend);
		return "succ";
	}
	
	/**
	 * 考勤数据分页查询
	 * @param condition
	 * @return
	 */
	@RequestMapping("/attendList")
	@ResponseBody
	public PageQueryBean listAttend(QueryCondition condition,HttpSession session){
		User user = (User) session.getAttribute("userInfo");
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;
	}
}
