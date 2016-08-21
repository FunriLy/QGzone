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
import com.qg.model.UserModel;
import com.qg.service.RelationService;

/**
 * 判断是否有未读的与我相关信息。
 * Servlet implementation class RelationCheckHasRead
 */
@WebServlet("/RelationCheckHasRead")
public class RelationCheckHasRead extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RelationService relationService = new RelationService();
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
		UserModel user = (UserModel)request.getSession().getAttribute("user");
		if(user!=null){
			if(relationService.hasRelationUnread(user.getUserId())){
				System.out.println("存在未读的与我相关信息");
				state = 431;//(改)
				}else{
					System.out.println("不存在未读的与我相关信息");
					state = 432;
				}
			}
		
		else{
			state = 0;
		}
		//返回数据给前端（状态码+与我相关对象集合）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();	
	}

}
