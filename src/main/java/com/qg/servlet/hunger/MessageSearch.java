package com.qg.servlet.hunger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
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
 * Servlet implementation class MessageSearch
 */
@WebServlet("/MessageSearch")
public class MessageSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService messageService = new MessageService();
	private Gson gson = new Gson();
	private boolean flag = false;
    private int state;   


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取存在session中的用户对象
		String userId = request.getParameter("userId");
		MessageModel message = null;
		if(userId!=null){
			message = messageService.getMessageById(Integer.parseInt(userId));
			if(message!=null){
				state = 161;//成功
			}
			else{
				state = 162;//失败
			}
		}
		else{
			state=0;//session消失
		}
		//返回数据给前端（状态码+用户信息对象）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("message", message);
		jsonObject.put("state", state+"");
		
		System.out.println(gson.toJson(jsonObject));
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();	
	}

}
