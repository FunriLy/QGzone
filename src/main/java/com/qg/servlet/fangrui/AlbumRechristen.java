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
 * 用户重命名相册
 * 状态码: 601-修改成功; 602-没有相应权限,修改失败; 603-相册不存在; 604-格式错误; 605-重名；
 * </p>
 */

@WebServlet("/RechristenAlbum")
public class AlbumRechristen extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumRechristen.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户在账号
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		AlbumService albumService = new AlbumService();
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
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

		if (success != albumService.albumIsExist(album.getAlbumId())) {
			state = 603;
		} else {
			//判断用户是否拥有相应的权限
			if (userId == albumService.getAlbumByAlbumId(album.getAlbumId()).getUserId()) {
				state = albumService.uplateAlbumName(album.getAlbumId(), album.getAlbumName());
			} else {
				state = 602;
			}
		}
		
		LOGGER.log(Level.DEBUG, "用户 {1} 修改相册 {1} 名为 {2} 状态: {3}", 
				userId, album.getAlbumId(), album.getAlbumName(), state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
