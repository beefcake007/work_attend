package org.fanye.login.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.fanye.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping
	public String login(){
		return "login";
	}
	
	/**
	 * 校验登录
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	@RequestMapping("/check")
	@ResponseBody
	public String checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//      token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            SecurityUtils.getSubject().getSession().setTimeout(-10001);
        } catch (Exception e) {
            return "login_fail";
        }
        return "login_succ";
		
		//查数据库，如果查到数据，调用MD5加密对比密码
//		User user=userService.findUserByUserName(username);
//		if(user!=null){
//			if(SecurityUtil.checkPassword(password, user.getPassword())){
//				//登录成功，设置session
//				HttpSession seesion=request.getSession();
//				seesion.setAttribute("userInfo", user);
//				return "login_succ";
//			}else{
//				//校验失败  返回校验失败signal
//				return "login_fail";
//			}
//		}else{
//			//校验失败  返回校验失败signal
//			return "login_fail";
//		}
	}
}
