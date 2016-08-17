package com.qg.servlet.hunger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
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
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置编码
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		String userId= map.getOrDefault("userId", null);//账号
		String userPassword = map.getOrDefault("password", null);//密码
		System.out.println(userId+userPassword);
		//判断账号密码是否符合
		flag = userService.doSingIn(userId, userPassword);
		UserModel user = null;
		//删除之前的session
		request.getSession().invalidate();
		if(flag){
			//登录成功
			state = 111;
			//将用户存入session
			user = userService.getUserById(Integer.parseInt(userId));
			request.getSession().setAttribute("user", user);
		}
		else{
			//登录失败
			state = 112;
		}
		
		//返回数据给前端（状态码+用户对象）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("user", user);
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();
	}

}
