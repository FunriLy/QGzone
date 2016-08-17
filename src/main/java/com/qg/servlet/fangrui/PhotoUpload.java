package com.qg.servlet.fangrui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import com.qg.service.AlbumService;
import com.qg.service.PhotoService;
import com.qg.util.ImgCompress;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户上传相片
 * 状态码: 601-上传成功; 602-上传失败; 603-上传格式错误; 604-相册不存在; 605-用户对该相册没有操作权限；
 * </p>
 */

@WebServlet("/UploadPhoto")
public class PhotoUpload extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PhotoUpload.class);
	private static final int fail = 0;
	private static final int success = 1;
	
	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//初始化状态码
		int state = 602;
		//获得用户id
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int userId = 1;
		//相册id
		int albumId = 0;
		AlbumService albumService = new AlbumService();
		
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		//解析表单
		try {
			//构造工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置文件阀值大小
			factory.setSizeThreshold(10 * 1024);
			//设置缓存文件区
			String tempPath = getServletContext().getRealPath("/album/tmp/");
			factory.setRepository(new File(tempPath));
			//获取解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//固定编码
			upload.setHeaderEncoding("utf-8");
			//解析请求内容
			List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));
			
			for(FileItem fileItem : list){
				if(fileItem.isFormField()){
					String value = fileItem.getString("utf-8");
					
					albumId = Integer.valueOf(value);
					LOGGER.log(Level.DEBUG, "上传图片: 用户账号 {0} 相册账号 {1}", userId, albumId);
				} else {
					// 定义限制上传的文件类型的字符串数组
					//String[] suffixs = new String[] { ".jpg", ".png", "bmp", "jpeg", "jpeg2000", "tiff", "psd", "swf" };
					// 创建文件后缀过滤器
					//SuffixFileFilter filter = new SuffixFileFilter(suffixs);
					if(fail == albumService.albumIsExist(albumId)){
						//相册不存在
						state = 604;
					} else if(userId != albumService.getAlbumByAlbumId(albumId).getUserId()){
						state = 605;
					} else {
						//用户上传相片
						//if (filter.accept((File)fileItem)){
							InputStream in = new BufferedInputStream(fileItem.getInputStream());
							PhotoService photoService = new PhotoService();
							int id = photoService.savePhotoByAlbumId(albumId);
							// 获取路径
							String path = getServletContext().getRealPath("/album/");
							if (id != fail) {
								OutputStream out = new BufferedOutputStream(
										new FileOutputStream(path + userId + "/" + albumId + "/" + id+ ".jpg"));
								//读写文件
								int len = -1;
								byte[] b = new byte[1024];
								while((len = in.read(b)) != -1){
									out.write(b, 0, len);
								}
								
								ImgCompress.ImageCompress(path + userId + "/" + albumId + "/", id);
								albumService.changePhotoCount(success, albumId);
								out.close();
								in.close();
								state = 601;
							} else {
								state = 602;
							}
							
						//} else {
							//格式错误
							//state = 603;
						//}
						
					}
				}
			}
		} catch (Exception e) {
			state = 602;
			LOGGER.log(Level.ERROR, "用户 {0} 上传相片到相册 {1} 发生异常 {3}", userId, albumId, e);
		} finally {
			LOGGER.log(Level.DEBUG, "用户 {0} 上传相片到相册 {1} 状态: {2} ", userId, albumId, state);
			
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
		}
	}
}
