package com.qg.servlet.hunger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.RelationModel;
import com.qg.model.UserModel;
import com.qg.service.RelationService;

/**
 * 删除与我相关
 * @author hunger
 *
 */
@WebServlet("/RelationDelete")
public class RelationDelete extends HttpServlet {
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

		//设置编码
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		String relationId= map.getOrDefault("relationId", null);//与我相关信息id
		//获取存在session中的用户对象 
		UserModel user = (UserModel)request.getSession().getAttribute("user");
		//获取与我相关集合
		List<RelationModel> relations = null;
		if(user!=null){
			
			if(relationId==null||relationService.deleteRelation(Integer.parseInt(relationId))){
				System.out.println("删除信息失败");
				state = 412;//失败
				}else{
					state = 411;//成功
					relationService.getRelationsById(1, user.getUserId());
				}
			}
			
			
		else{
			System.out.println("用户session消失");
			state = 0;
		}
		//返回数据给前端（状态码+与我相关对象集合）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("relations", relations);
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();		
	
	}

}
