package com.qg.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * 禁止缓存
 * 
 * 
 * 
 */
@WebFilter(filterName="NoCacheFilter",urlPatterns="/*")
public class NoCacheFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 使用与HTTP协议相关 API，需要将参数转为子类型
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletResponse.setHeader("Cache-Control", "no-cache");
		httpServletResponse.setDateHeader("Expires", -1);
		httpServletResponse.setHeader("Pragma", "no-cache");

		chain.doFilter(request, httpServletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
