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
 * 用户发送好友申请
 * 状态码: 301-成功; 302-失败; 303-该用户不存在; 304-用户给自己发申请; 305-已经存在好友关系;
 * </p>
 */

@WebServlet("/SendFriendApply")
public class FriendApplySend extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplySend.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		//初始化状态码
		int state = 302;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		FriendService friendService = new FriendService();
		
		if (request.getParameter("addFriendId")==null || request.getParameter("addFriendId")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针");
			return;
		}
		
		int addFriendId = Integer.valueOf(request.getParameter("addFriendId"));
		int result = friendService.sendFriendApply(userId, addFriendId);
		
		if (1 == result) {
			state = 301;
		} else if (-1 == result) {
			state = 304;
		} else if (-2 == result) {
			state = 303;
		} else if (-3 == result) {
			state = 305;
		} else {
			state = 302;
		}
	
		LOGGER.log(Level.DEBUG, "用户 {0} 请求添加用户 {1} 为好友，状态: {2}", userId, addFriendId, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
