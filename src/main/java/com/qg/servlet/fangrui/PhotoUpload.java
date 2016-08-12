package com.qg.servlet.fangrui;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.qg.model.UserModel;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户上传相片
 * 状态码: 601-上传成功; 602-上传失败;
 * </p>
 */

@WebServlet("/UploadPhoto")
public class PhotoUpload extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PhotoUpload.class);

	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//初始化状态码
		int state = 602;
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		
		//解析表单
		try {
			//构造工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置文件阀值大小
			factory.setSizeThreshold(10 * 1024);
			//设置缓存文件区
			String tempPath = getServletContext().getRealPath("/WEB-INF/album/tmp/");
			factory.setRepository(new File(tempPath));
			//获取解析器
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
