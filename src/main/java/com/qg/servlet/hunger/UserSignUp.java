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
import com.qg.model.UserModel;
import com.qg.service.UserService;
import com.qg.util.JsonUtil;

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
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置编码
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		UserModel user = gson.fromJson(reciveObject, UserModel.class);//用户对象
		//判断注册格式，成功返回账号
		String userId= null;
		if(user!=null&&(userId= userService.doSignUp(user))!=null){
			state = 101;
		}
		else{
			//失败
			state = 102;
		}
		
		
		//返回数据给前端（状态码+账号）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("userId", userId);
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		System.out.println(jsonObject);
		output.close();
		
	}

}
