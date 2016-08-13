package com.qg.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qg.model.UserModel;
import com.qg.service.UserService;

/**
 * 
 * @author hunger
 * <p>
 * 用户登录
 * 状态码: 111 登录成功，112登录失败
 * </p>
 */
@WebServlet("/UserSignIn")
public class UserSignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	private Gson gson = new Gson();
	private boolean flag = false;
    private int state;     

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		String userId= map.getOrDefault("userId", null);
		String userPassword = map.getOrDefault("password", null);
		System.out.println(userId+userPassword);
		flag = userService.doSingIn(userId, userPassword);
		if(flag){
			state = 111;
		}
		else{
			state = 112;
		}
	}

}
