package com.qg.util;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CommpressionFilter
 */
@WebFilter(filterName="CommpressionFilter",urlPatterns="/*")
public class CommpressionFilter implements Filter {



	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String encodings = req.getHeader("accept-encoding");
		if((encodings != null)&&(encodings.indexOf("gzip")>-1)){
			CompressionWrapper responseWrapper = new CompressionWrapper(res);
			responseWrapper.setHeader("content-encoding", "gzip");
			chain.doFilter(request, responseWrapper);
			GZIPOutputStream gzipOutputStream = responseWrapper.getGZIPOutputStream();
			if(gzipOutputStream != null){
				gzipOutputStream.finish();
			}
		}
		else{
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
