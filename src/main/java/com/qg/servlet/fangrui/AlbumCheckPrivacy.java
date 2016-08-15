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

import com.google.gson.Gson;
import com.qg.model.AlbumModel;
import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.service.PhotoService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户查看私密相册
 * 状态码: 601-成功; 602-密码错误; 603-相册不存在; 604-相片为0; 605-非好友关系;
 * </p>
 */

@WebServlet("/CheckPrivacyAlbum")
public class AlbumCheckPrivacy extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumCheckPrivacy.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获取用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		if (request.getParameter("jsonObject")== null || request.getParameter("jsonObject")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针！");
			return;
		}
		//获得Json数据并解析
		String strAlbum = request.getParameter("jsonObject");
		Gson gson = new Gson();
		AlbumModel album = gson.fromJson(strAlbum, AlbumModel.class);
		
		AlbumService albumService  = new AlbumService();
		
		List<Integer> allPhotoId = new ArrayList<Integer>();
		
		state = albumService.checkPrivacyAlbum(album, userId);
		//如果状态码为601
		if (601 == state) {
			PhotoService photoService = new PhotoService();
			allPhotoId = photoService.allPhoto(album.getAlbumId());
		}
		
		
		LOGGER.log(Level.DEBUG, "用户 {0} 查看私密相册 {1} 状态: {2}", userId, album.getAlbumId(), state);
		
		output.write(JsonUtil.tojson(state, allPhotoId).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
