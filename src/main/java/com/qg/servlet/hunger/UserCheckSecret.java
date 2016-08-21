package com.qg.servlet.hunger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.service.MessageService;
import com.qg.service.UserService;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * Servlet implementation class UserCheckSecret
 */
@WebServlet("/UserCheckSecret")
public class UserCheckSecret extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
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
		//获取Json并解析
		String reciveObject = request.getParameter("jsonObject");
		//获取Json并解析
		Map<String,String> map = gson.fromJson(reciveObject, Map.class);
		int  userId= Integer.parseInt(map.getOrDefault("userId", "0"));//账号
		int oldSecretId = Integer.parseInt(map.getOrDefault("oldSecretId", "0"));//旧密保编号
		String oldAnswer = map.getOrDefault("oldAnswer", null);//旧密保答案
		flag = userService.checkPassword(userId, oldSecretId, oldAnswer);
		if(flag){
			//成功
			state = 1211;
		}
		else{
			//失败
			state = 1212;
		}
		//返回数据给前端（状态码）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		System.out.println(jsonObject);
		output.close();
			
	}

}
