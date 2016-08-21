package com.qg.util;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Interceptor
 */
@WebServlet("/Interceptor")
public class Interceptor extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int state = 0 ;  

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 拦截器
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}

}
