package com.qg.servlet.hunger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.qg.model.MessageModel;
import com.qg.model.TwitterModel;
import com.qg.model.UserModel;
import com.qg.service.MessageService;
import com.qg.service.TwitterService;
import com.qg.service.UserService;
import com.qg.servlet.llh.TwitterAdd;
import com.qg.util.ImgCompress;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author hunger
 * <p>
 * 用户登录
 * 状态码: 171 上传成功，172上传失败 173 文件格式不正确
 * </p>
 */
@WebServlet("/UserUploadImage")
public class UserUploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService messageService = new MessageService();
	private static final Logger LOGGER = Logger.getLogger(UserUploadImage.class);
	private Gson gson = new Gson();
	private boolean flag = false;
    private int state;    



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取存在session中的用户对象
		UserModel user = (UserModel)request.getSession().getAttribute("user");
		MessageModel message = null;
		//如果用户对象不存在
		if (user==null) {
			state = 0;
			//返回数据给前端（状态码+用户信息对象）	
			Map<String,Object> jsonObject = new HashMap();
			jsonObject.put("message", message);
			jsonObject.put("state", state+"");
			DataOutputStream output = new DataOutputStream(response.getOutputStream());
			output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
			output.close();	
			return;
		}
		//步骤一：构造工厂
		DiskFileItemFactory factory= new DiskFileItemFactory();
		// 设置文件阀值大小
		factory.setSizeThreshold(10 * 1024);
		// 设置缓存文件区
		String tempPath = getServletContext().getRealPath("/jpg/");
		factory.setRepository(new File(tempPath));
		//步骤二：获得解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名 乱码问题
		upload.setHeaderEncoding("utf-8");
		System.out.println("....");
		//步骤三：对请求内容进行解析
		try {
			List<FileItem> list = upload.parseRequest(request);
			//遍历集合
			for (FileItem fileItem : list) {
				if(fileItem.isFormField()){
					String name = fileItem.getFieldName();
					String value =fileItem.getString("utf-8");
					System.out.println("普通上传项："+name+"..."+value);
				}
				else{
					String filename =fileItem.getName();
					// 截取文件路径获取文件名
					if (filename.contains("\\")) {
						filename = filename.substring(filename
								.lastIndexOf("\\") + 1);
					}
					System.out.println("文件上传项："+filename);
					InputStream in = new BufferedInputStream(fileItem.getInputStream());
					// 若不是文本，判断名字是否为空后（是否上传文件），图片数自增+1
					String fileEnd = fileItem.getName().toLowerCase();
					if (!(fileEnd.endsWith(".jpg") || fileEnd.endsWith(".png") || fileEnd.endsWith(".bmp")
							|| fileEnd.endsWith(".swf") || fileEnd.endsWith(".jpeg") || fileEnd.endsWith(".jpeg2000")
							|| fileEnd.endsWith(".tiff"))) {
						state = 173;
						//返回数据给前端（状态码+用户信息对象）	
						Map<String,Object> jsonObject = new HashMap();
						jsonObject.put("message", message);
						jsonObject.put("state", state+"");
						DataOutputStream output = new DataOutputStream(response.getOutputStream());
						output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
						output.close();	
						return;
					}
					//获取路径 
					String path = getServletContext().getRealPath("/jpg/");
					System.out.println(path);
					OutputStream out = new BufferedOutputStream(
							new FileOutputStream(path+ user.getUserId() + ".jpg"));
					// 将文件写在服务器
					int len = -1;
					byte[] b = new byte[1024];
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}
					ImgCompress.ImageCompress(path,user.getUserId());
					out.close();
					in.close();
					// 删除临时文件(必须将文件流关掉)
					fileItem.delete();
					//将照片名存入数据库
					messageService.changeImage(user.getUserId()+"");
					//取出更新后的数据
					message = messageService.getMessageById(user.getUserId()+"");
					state = 171;//成功
				}
			}

		} catch (Exception e) {
			state = 172;
			LOGGER.log(Level.ERROR, "修改头像异常", e);
		}
		//返回数据给前端（状态码+用户信息对象）	
		Map<String,Object> jsonObject = new HashMap();
		jsonObject.put("message", message);
		jsonObject.put("state", state+"");
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
		output.close();	
	}

}
