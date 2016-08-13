package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteModel;
import com.qg.model.UserModel;
import com.qg.service.NoteService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteOfMe")
/***
 * 
 * @author dragon
 * <pre>
 * 获得留言
 * 501获取成功，502获取失败
 * </pre>
 */
public class NoteOfMe extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteOfMe.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 501;
		List<NoteModel> notes = null;
		// 获取当前用户id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		// 获取页码
		String page = request.getParameter("page");

		try {
			// 获取全部留言
			notes = new NoteService().getNote(Integer.parseInt(page), userId);
		} catch (Exception e) {
			state = 502;
			LOGGER.log(Level.ERROR, "获取说说异常", e);
		} finally {
			DataOutputStream output = new DataOutputStream(resp.getOutputStream());
			output.write(JsonUtil.tojson(state, notes).getBytes("UTF-8"));
			output.close();
		}
	}
}