package com.qg.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.UserModel;
import com.qg.service.UserService;

/**
 * 
 * @author hunger
 * <p>
 * 用户注册
 * 状态码: 101 注册成功，102注册失败
 * </p>
 */
@WebServlet("/UserSignUp")
public class UserSignUp extends HttpServlet {
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
		UserModel user = gson.fromJson(reciveObject, UserModel.class);
		System.out.println();
		flag = userService.doSignUp(user);
		if(flag){
			state = 101;
		}
		else{
			state = 102;
		}
	}

}
