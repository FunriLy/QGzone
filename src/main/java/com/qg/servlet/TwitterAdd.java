package com.qg.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
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
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import com.google.gson.Gson;
import com.qg.dao.impl.AlbumDaoImpl;
import com.qg.model.TwitterModel;
import com.qg.model.UserModel;
import com.qg.service.TwitterService;
import com.qg.util.Logger;
import com.qg.util.JsonUtil;
import com.qg.util.Level;

/**
 * 
 * @author dragon
 * 
 *         <pre>
 *         上传说说的类
 *         </pre>
 */
@WebServlet("/TwitterAdd")
public class TwitterAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterAdd.class);
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state;
		int twitterPicture=0;
		int twitterPictureId=0;
		// 状态标志量
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		Gson gson = new Gson();
		TwitterModel twitter = null;
		int talkId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		twitter.setTalkId(talkId);
		try {
			// 构造工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置文件阀值大小
			factory.setSizeThreshold(10*1024);
			//设置缓存文件区
			String tempPath=getServletContext().getRealPath("/WEB-INF/twitter/tmp/");
			factory.setRepository(new File(tempPath));
			// 获得解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名 乱码问题
			upload.setHeaderEncoding("utf-8");
			// 对请求内容进行解析
			List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));
			// 获得图片的张数
			for (FileItem fileItem : list) {
				if (!fileItem.isFormField()) {
					twitterPicture++;
				}
			}
			twitter.setTwitterPicture(twitterPicture);
			//存进数据库且获得其说说id、
			int twitterId=new TwitterService().addTwitter(twitter);
			// 遍历集合
			for (FileItem fileItem : list) {
				if (fileItem.isFormField()) {
					String name = fileItem.getFieldName();
					String value = fileItem.getString("utf-8");
					LOGGER.log(Level.INFO, "说说文字", name,value);
					twitter.setTwitterWord(value);
				} else {
					// 定义限制上传的文件类型的字符串数组
					String[] suffixs = new String[] {".jpg",".png","bmp","jpeg","jpeg2000","tiff","psd","swf"};
					// 创建文件后缀过滤器
					SuffixFileFilter filter = new SuffixFileFilter(suffixs);
					//文件是否符合格式
					if(filter.accept((File) fileItem))
					{
						twitterPictureId++;
						InputStream in = new BufferedInputStream(fileItem.getInputStream());
						// 获取路径
						String path = getServletContext().getRealPath("/WEB-INF/twitter/");
						System.out.println(path);
						// 将文件写在服务器
						OutputStream out = new BufferedOutputStream(new FileOutputStream(path+twitterId+"_"+twitterPictureId+".jpg"));
						
						int len=-1;
						byte[] b = new byte[1024];
						while ((len = in.read(b)) != -1) {
							out.write(b,0,len);
						}
						out.close();
						in.close();
						state=201;
					}else{
						state=203;
					}
				}
			}
		} catch (Exception e) {
			state=202;
			LOGGER.log(Level.ERROR, "发表说说异常", e);
		} finally {
			JsonUtil<String, String> object = new JsonUtil(state);
			output.write(gson.toJson(object).getBytes("UTF-8"));  
			output.close();
		}

	}
}