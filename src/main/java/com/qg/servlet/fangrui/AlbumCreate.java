package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.model.AlbumModel;
import com.qg.service.AlbumService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户创建文件夹
 * 状态码: 601-创建成功; 602-创建失败; 603-密码格式错误; 604-命名重复；
 * </p>
 */

@WebServlet("/CreateAlbum")
public class AlbumCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumCreate.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得用户id
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());

		
		String path = getServletContext().getRealPath("/album/") + String.valueOf(userId);
		File file = new File(path);
		//确保用户有个人文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		
		if (request.getParameter("album")==null || request.getParameter("album")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针");
			return;
		}
		
		//获得Json并解析
		Gson gson = new Gson();
		String strAlbum = request.getParameter("album");
		AlbumModel album = gson.fromJson(strAlbum, AlbumModel.class);
		album.setUserId(userId);
		AlbumService albumService = new AlbumService();
		
		int result =  albumService.createAlbum( album, path);
		
		if (result == -2) {
			state = 604;
		} else if (result == -1) {
			state = 603;
		} else if (result == 0) {
			 state = 602;
		} else if (result > 0) {
			state = 601;
			album.setAlbumId(result);
			album.setAlbumPassword("");
		} else {
			state = 602;
		}
		
		
		LOGGER.log(Level.DEBUG, "用户 {0} 创建相册 {1} 状态: {2}", userId, result, state);
		
		output.write(JsonUtil.tojson(state, album).getBytes("UTF-8"));
		output.close();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	
}
