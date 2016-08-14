package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
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
 * 用户处理好友申请
 * 状态码: 301-成功; 302-失败; 303-已经是好友关系; 304-申请不存在;
 * </p>
 */

@WebServlet("/ConductFriendApply")
public class FriendApplyConduct extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplyConduct.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		String reciveObject = request.getParameter("friendApplyId");
		FriendService friendService = new FriendService();
		int state = 302;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		int result = friendService.conductFriendApply(Integer.valueOf(reciveObject));
		if(result == 1){
			state = 301;
		} else if(result == 3){
			state = 303;
		} else if(result == 4){
			state = 304;
		} else {
			state = 302;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 请求处理好友申请， 好友申请编号: {1} 状态: {2}", userId, reciveObject, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
