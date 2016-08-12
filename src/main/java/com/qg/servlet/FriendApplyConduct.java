package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.dao.FriendDao;
import com.qg.dao.impl.FriendDaoImpl;
import com.qg.model.FriendApplyModel;
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
 * 用户处理好友申请
 * 状态码: 301-成功; 302-失败; 303-已经处理; 304-申请不存在;
 * </p>
 */

@WebServlet("ConductFriendApply")
public class FriendApplyConduct extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplyConduct.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		String reciveObject = request.getParameter("jsonObject");
		FriendService friendService = new FriendService();
		Gson gson =new Gson();
		int state = 302;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		//解析Json
		FriendApplyModel friendApply = gson.fromJson(reciveObject, FriendApplyModel.class);
		int result = friendService.conductFriendApply(friendApply);
		if(result == 1){
			state = 301;
		} else if(result == 3){
			state = 303;
		} else if(result == 4){
			state = 304;
		} else {
			state = 302;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 请求处理好友申请， 好友申请编号: {1} 状态: {2}", userId, friendApply.getRequesterId(), state);
		
		JsonUtil<String, String> object = new JsonUtil(state);
		output.write(gson.toJson(object).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
