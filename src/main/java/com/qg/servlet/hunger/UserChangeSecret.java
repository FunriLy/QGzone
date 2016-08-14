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
 * 根据旧密保修改新密保
 * @author hunger
 * <p>
 * 修改密保
 * 状态码: 151 修改密保成功，152修改密保失败
 * </p>
 */
@WebServlet("/UserChangeSecret")
public class UserChangeSecret extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	private Gson gson = new Gson();
	private boolean flag = false;
    private int state;   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			int oldSecretId = Integer.parseInt(map.getOrDefault("oldSecretId", "0"));//旧密保编号
			String oldAnswer = map.getOrDefault("oldAnswer", null);//旧密保答案
			int newSecretId = Integer.parseInt(map.getOrDefault("newSecretId", "0"));//新密保编号
			String newAnswer = map.getOrDefault("newAnswer", null);//新密保答案
			flag = userService.changeSecret(userId, oldSecretId, oldAnswer, newSecretId, newAnswer);
			if(flag){
				state = 141;//成功
			}
			else{
				state = 142;//失败
			}
		}
		else{
			state = 000;//session消失
		}
		//返回数据给前端（状态码）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("state", state);
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();
	}

}
