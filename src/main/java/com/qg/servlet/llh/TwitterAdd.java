package com.qg.servlet.llh;

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

import com.qg.model.TwitterModel;
import com.qg.model.UserModel;
import com.qg.service.TwitterService;
import com.qg.util.Logger;
import com.qg.util.ImgCompress;
import com.qg.util.JsonUtil;
import com.qg.util.Level;

/**
 * 
 * @author dragon
 *  <pre>
 *  添加说说的类
 *  201-成功  202-添加失败  203-上传图片格式不正确   204-至多上传九张图   205-说说长度过长
 *  </pre>
 */
@WebServlet("/TwitterAdd")
public class TwitterAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterAdd.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(1);
		
		// 状态标志量
		int state = 201;
		// 图片张数
		int twitterPicture = 0;
		// 图片序列
		int twitterPictureId = 0;
		// 添加说说时候返回的说说id
		int twitterId = 0;
		// 说说内容
		String value = null;
		// 文件名
		String filename;
		
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		
		// 获取当前登陆用户id
		int talkId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		
		try {
			// 构造工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// 设置文件阀值大小
			factory.setSizeThreshold(10 * 1024);

			// 设置缓存文件区
			String tempPath = getServletContext().getRealPath("/twitterPhotos/tmp/");
			factory.setRepository(new File(tempPath));

			// 获得解析器
			ServletFileUpload upload = new ServletFileUpload(factory);

			// 解决上传文件名 乱码问题
			upload.setHeaderEncoding("utf-8");

			// 对请求内容进行解析
			List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));

			// 获得图片的张数
			for (FileItem fileItem : list) {

				if (fileItem.isFormField()) {

					// 获取说说内容的name值
					String name = fileItem.getFieldName();

					// 根据name获取内容
					if (name.equals("twitterWord")) {
						value = fileItem.getString("utf-8");
					}

				} else {
					// 若不是文本，判断名字是否为空后（是否上传文件），图片数自增+1
					String fileEnd = fileItem.getName().toLowerCase();
					if (!(value!=null||fileEnd.endsWith(".jpg") || fileEnd.endsWith(".png") || fileEnd.endsWith(".bmp")
							|| fileEnd.endsWith(".swf") || fileEnd.endsWith(".jpeg") || fileEnd.endsWith(".jpeg2000")
							|| fileEnd.endsWith(".tiff"))) {
						state = 203;
						output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
						output.close();
						return;
					} else if(!fileItem.getName().equals(""))
						twitterPicture++;
				}
			}
			// 判断字数是否超限
			if (value != null && value.length() >= 150)
				state = 205;
			else {
				// 需要文本或者图片才能进行说说发表
				if ((value != null && value.length() > 0) || twitterPicture != 0) {
					// 获取说说实体
					TwitterModel twitter = new TwitterModel(value, twitterPicture, talkId);
					// 存进数据库并且获得说说id
					twitterId = new TwitterService().addTwitter(twitter);
				}
				if (twitterPicture <= 9) {
					// 遍历集合
					for (FileItem fileItem : list) {
						if (!fileItem.isFormField()) {
							filename = fileItem.getName();
							if (!filename.equals("")) {
								
								twitterPictureId++;
								
								// 获取路径
								String path = getServletContext().getRealPath("/twitterPhotos/");
								LOGGER.log(Level.DEBUG, "说说图片id:{0},名字:{1}", twitterId,
										twitterId + "_" + twitterPictureId + ".jpg");
								
								//获取流
								OutputStream out = new BufferedOutputStream(
										new FileOutputStream(path + twitterId + "_" + twitterPictureId + ".jpg"));
								InputStream in = new BufferedInputStream(fileItem.getInputStream());
								
								// 将文件写在服务器
								int len = -1;
								byte[] b = new byte[1024];
								while ((len = in.read(b)) != -1) {
									out.write(b, 0, len);
								}
								//关闭流
								out.close();
								in.close();
								ImgCompress.ImageCompress(path, twitterId + "_" + twitterPictureId + ".jpg");
								
								state = 201;
							} else
								state = 202;
						}
					}
				} else
					state = 204; // 只能上传九张图
			}

		} catch (Exception e) {
			state = 202;
			LOGGER.log(Level.ERROR, "发表说说异常", e);
		} finally {
			LOGGER.log(Level.DEBUG, "{2}发表说说，说说内容{0},图片张数{1},状态码{3}",value ,twitterPicture,talkId,state);
			output.write(JsonUtil.tojson(state, twitterId).getBytes("UTF-8"));
			output.close();
		}

	}
}