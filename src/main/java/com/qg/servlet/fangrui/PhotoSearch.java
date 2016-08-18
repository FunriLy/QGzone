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

import com.qg.model.UserModel;
import com.qg.service.PhotoService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 获得用户展示公开相片
 * 状态码: 601-成功(两张); 602-没有公开相片;
 * </p>
 */
@WebServlet("/photoSearch")
public class PhotoSearch extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PhotoSearch.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得登录用户
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		//初始化
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		List<String> strphoto = new ArrayList<String>();
		int t_userId = 0;
		
		if (request.getParameter("userId")==null || request.getParameter("userId")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针！");
			return;
		}
		//防止传入非数字
		try{
			t_userId = Integer.valueOf(request.getParameter("userId"));
		} catch (NumberFormatException e){
			output.write(JsonUtil.tojson(state, strphoto).getBytes("UTF-8"));
			output.close();
		} 
		
		PhotoService photoService = new PhotoService();
		strphoto = photoService.getPhotoByUserId(t_userId);
		if (!strphoto.isEmpty()) {
			state = 601;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 查询用户 {1} 的展示照片 状态: {2}", userId, t_userId, state);
		
		output.write(JsonUtil.tojson(state,strphoto).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
