package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.UserModel;
import com.qg.service.FriendService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户删除好友
 * 状态码: 301-成功; 302-操作失败; 303-不存在好友关系;
 * </p>
 */

@WebServlet("/DeleteFriend")
public class FriendDelete extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendDelete.class);
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id 与 操作对象id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		
		//初始化状态码
		int state = 302; 
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		if (request.getParameter("friendId")==null || request.getParameter("friendId")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针");
			return;
		}
		
		int friendId = Integer.valueOf(request.getParameter("friendId"));
		FriendService friendService = new FriendService();
		int result = friendService.deleteFriend(userId, friendId);
		if(1 == result){
			//成功执行操作
			state = 301;
		} else if(3 == result) {
			//不存在好友关系
			state = 303;
		} else {
			state = 302;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 执行解除与用户 {1} 的好友关系  状态: {2}", userId, friendId, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
