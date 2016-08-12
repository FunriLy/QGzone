package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.FriendApplyModel;
import com.qg.model.UserModel;
import com.qg.service.FriendService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户获得好友申请列表
 * </p>
 */

@WebServlet("/MyFriendApply")
public class FriendApplyMy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplyMy.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得用户id
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		int state = 302;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		FriendService friendService = new FriendService();
		List<FriendApplyModel> allFriendApply = new ArrayList<>();
		allFriendApply = friendService.getAllFriendApply(userId);
		
		if (!allFriendApply.isEmpty()) {
			state = 301;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 查看好友申请列表 状态: {1}", userId, state);
		output.write(JsonUtil.tojson(state,allFriendApply).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
