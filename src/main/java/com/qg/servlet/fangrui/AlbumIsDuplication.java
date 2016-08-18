package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 判断相册名是否重复
 * 状态码: 601-不重复; 602-重复;
 * </p>
 */

@WebServlet("/DuplicationAlbum")
public class AlbumIsDuplication extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumIsDuplication.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		if (request.getParameter("albumName")==null || request.getParameter("albumName")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针");
			return;
		}
		
		AlbumService albumService = new AlbumService();
		
		//获得Json
		String albumName = request.getParameter("albumName");
		state = albumService.isDuplicationOfName(userId, albumName, 0);
		
		LOGGER.log(Level.DEBUG, "用户 {0} 检查相册名 {1} 是否重复，状态: {2} ", userId, albumName, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
