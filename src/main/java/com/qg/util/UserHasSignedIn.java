package com.qg.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class UserHasSignedIn
 */
@WebFilter(filterName="UserHasSignedIn",urlPatterns="/*")
public class UserHasSignedIn implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 使用与HTTP协议相关 API，需要将参数转为子类型
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String projectName = "/QGzone/";
		String url = httpServletRequest.getRequestURI();
		System.out.println("访问路径："+url);
		String[] ex = {"UserSignIn","UserForgetPassword","UserSignUp","UserForgetPassword","UserCheckSecret"
				,"html","js","css","jpg","images"
				,"twitterPhotos","album"
				,"Interceptor"
				};
		for (int i = 0; i < ex.length; i++) {
			if(url.contains(projectName+ex[i])){
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
			}
		}
		HttpSession  session =httpServletRequest.getSession();	
		if(session.getAttribute("user")!=null){
			chain.doFilter(httpServletRequest, httpServletResponse);
		}
		else{
			System.out.println("错误！！！您访问的路径已被限制："+url);
			httpServletRequest.getRequestDispatcher("/Interceptor").forward(httpServletRequest, httpServletResponse);			
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
