package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;

import com.google.gson.Gson;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.service.SearchService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 通过id来查找用户
 * 状态码: 301-成功；302-没找到;
 * </p>
 */

@WebServlet("SearchByUserId")
public class SearchByUserId extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchByUserId.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int searchUserId = Integer.valueOf(request.getParameter("searchUserId"));
		int state = 302;
		Gson gson = new Gson();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		SearchService searchService = new SearchService();
		//将对象包装进集合发送过去
		List<MessageModel> allMessage = new ArrayList<MessageModel>();
		MessageModel message = searchService.searchMessageByUserId(searchUserId);
		if (message != null) {
			state = 301;
			allMessage.add(message);
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 搜索账号 {1}", userId, searchUserId);
		
		JsonUtil<List<MessageModel>, String> object = new JsonUtil(state,allMessage);
		output.write(gson.toJson(object).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
