package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.dao.FriendDao;
import com.qg.dao.impl.FriendDaoImpl;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户删除好友申请
 * 状态码: 301-成功; 302-失败; 
 * </p>
 */

@WebServlet("DeleteFriendApply")
public class FriendApplyDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FriendApplyDelete.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int friendApplyId = Integer.valueOf(request.getParameter("friendApplyId"));
		int state = 302;
		Gson gson = new Gson();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		FriendDao friendDao = new FriendDaoImpl();
		if(success == friendDao.deleteFriendApply(friendApplyId)){
			state = 301;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 删除好友申请编号 {1}", userId, friendApplyId);
		
		JsonUtil<List<MessageModel>, String> object = new JsonUtil(state);
		output.write(gson.toJson(object).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
