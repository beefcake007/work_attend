package org.fanye.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fanye.user.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * ÊµÏÖÀ¹½ØÆ÷
 * @author fanye
 *
 */
public class SessionInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String uri=request.getRequestURI();
		if(uri.indexOf("login")>=0){
			return true;
		}
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("userInfo");
		if(user!=null){
			return true;
		}
		
		request.getRequestDispatcher("/login").forward(request, response);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
