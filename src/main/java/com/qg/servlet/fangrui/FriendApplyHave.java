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
 * 检查用户是否还有未处理的好友申请
 * 状态码：301-有; 302-无;
 * </p>
 */

@WebServlet("/HaveFriendApply")
public class FriendApplyHave extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplyHave.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
//		int userId = 1;
		int state = 302;
		FriendService friendService = new FriendService();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		if (success == friendService.havefriendApply(userId)) {
			state = 301;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 请求查看是否有未处理的好友申请 状态: {1}", userId, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
