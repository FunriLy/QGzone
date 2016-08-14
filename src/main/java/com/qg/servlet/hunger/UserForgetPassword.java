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
import com.qg.service.UserService;

/**
 * 
 * @author hunger
 * <p>
 * 忘记密码，根据密保编号以及问题答案修改密码
 * 状态码: 121 修改密码成功，122修改密保失败
 * </p>
 */
@WebServlet("/UserForgetPassword")
public class UserForgetPassword extends HttpServlet {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		System.out.println(reciveObject);
		//获取Json并解析
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		int  userId= Integer.parseInt(map.getOrDefault("userId", "0"));//账号
		int oldSecretId = Integer.parseInt(map.getOrDefault("oldSecretId", "0"));//旧密保编号
		String oldAnswer = map.getOrDefault("oldAnswer", null);//旧密保答案
		String newPassword = map.getOrDefault("newPassword", null);//新密码
		System.out.println(userId+newPassword);
		flag = userService.forgetPassword(userId, oldSecretId, oldAnswer, newPassword);
		if(flag){
			//成功
			state = 121;
		}
		else{
			//失败
			state = 122;
		}
		//返回数据给前端（状态码）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();
		
		
	}

		
	

}
