package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.UserModel;
import com.qg.service.FriendService;
import com.qg.service.SearchService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户发送好友申请
 * 状态码: 301-成功; 302-失败; 303-该用户不存在
 * </p>
 */

@WebServlet("SendFriendApply")
public class FriendApplySend extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplySend.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int addFriendId = Integer.valueOf(request.getParameter("addFriendId"));
		//初始化状态码
		int state = 302;
		Gson gson = new Gson();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		FriendService friendService = new FriendService();
		SearchService searchService = new SearchService();
		
		if(success == searchService.userIsExist(addFriendId)){
			//若发送申请成功
			if(success == friendService.sendFriendApply(userId, addFriendId)){
				state = 301;
			}
		} else {
			state = 303;
		}
	
		LOGGER.log(Level.DEBUG, "用户 {0} 请求添加用户 {1} 为好友，状态: {2}", userId, addFriendId, state);
		
		JsonUtil<String, String> object = new JsonUtil(state);
		output.write(gson.toJson(object).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
