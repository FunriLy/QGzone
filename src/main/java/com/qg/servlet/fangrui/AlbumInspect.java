package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.AlbumModel;
import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户判断相册密码是否正确
 * 状态码: 601-正确; 602-错误;
 * </p>
 */

@WebServlet("/InspectAlbum")
public class AlbumInspect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumInspect.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		int state = 602;
		
		//获得Json并解析
		Gson gson = new Gson();
		String strAlbum = request.getParameter("jsonObject");		
		AlbumModel album = gson.fromJson(strAlbum, AlbumModel.class);
		
		AlbumService albumService = new AlbumService();
		
		if (success == albumService.isPassword(album.getAlbumState(), album.getAlbumPassword())) {
			state = 601;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 判断相册 {1} 密码，状态: {2}", userId, album.getAlbumId(), state);
		
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
		
//		String path = getServletContext().getRealPath("/album");
//		System.out.println(path);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
