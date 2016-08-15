package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.MessageModel;
import com.qg.service.FriendService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户获取好友列表信息
 * 状态码: 301-成功; 302-失败;
 * </p>
 */

@WebServlet("/MyFriends")
public class FriendMyFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendMyFriends.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		int state = 302;
		FriendService friendService = new FriendService();
		List<MessageModel> myFriends = friendService.myAllFriend(userId);
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		if(myFriends.isEmpty()){
			//集合为空
			state = 302;
		} else {
			state = 301;
		}
		
		System.out.println(getServletContext().getRealPath("/album/tmp/"));
		
		LOGGER.log(Level.DEBUG, "用户获取好友列表 用户: {0} 状态: {1}", userId, state);
		
		output.write(JsonUtil.tojson(state, myFriends).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
