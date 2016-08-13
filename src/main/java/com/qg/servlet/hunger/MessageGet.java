package com.qg.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.service.MessageService;

/**
 * Servlet implementation class MessageGet
 */
@WebServlet("/MessageGet")
public class MessageGet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService messageService = new MessageService();
	private Gson gson = new Gson();
	private boolean flag = false;
    private int state;     


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		//获取存在session中的用户对象
		UserModel user = (UserModel)request.getSession().getAttribute("user");
		if(user!=null){
			MessageModel message = messageService.getMessageById(user.getUserId());
			if(flag){
				state = 171;//成功
			}
			else{
				state = 172;//失败
			}
		}
		else{
			state=0;
		}
	
	
	
	}

}
