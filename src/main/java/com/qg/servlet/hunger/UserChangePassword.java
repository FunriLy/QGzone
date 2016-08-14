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

/**
 * 根据旧密码修改新密码
 * @author hunger
 * <p>
 * 用户登录
 * 状态码: 131 修改密码成功，132修改密码失败
 * </p>
 */
@WebServlet("/UserChangePassword")
public class UserChangePassword extends HttpServlet {
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
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		//获取存在session中的用户对象
		UserModel user = (UserModel)request.getSession().getAttribute("user");
		if(user!=null){
			int userId = user.getUserId();
			String oldPassword = map.getOrDefault("oldPassword", null);//旧密码
			String newPassword = map.getOrDefault("newPassword", null);//新密码
			flag = userService.changePassword(userId, oldPassword, newPassword);
			if(flag){
				state = 131;//成功
			}
			else{
				state = 132;//失败
			}
		}
		else{
			//session消失
			state = 000;
		}
		//返回数据给前端（状态码）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();
		
	}

}
