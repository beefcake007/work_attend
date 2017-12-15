package org.fanye.user.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.fanye.user.entity.User;
import org.fanye.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/home")
	public String home(){
		return "home";
	}
	
	/**
	 * 获取用户信息
	 * @param session
	 * @return
	 */
	@RequestMapping("/userInfo")
	@ResponseBody
	public User getUser(HttpSession session){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		return user;
	}
	
	/**
	 * 退出登录
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		SecurityUtils.getSubject().getSession().touch();
		SecurityUtils.getSubject().getSession().stop();
		return "login";
	}
}
