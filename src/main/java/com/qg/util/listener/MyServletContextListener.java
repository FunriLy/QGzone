package com.qg.util.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;

import com.qg.model.UserModel;

/**
 * 对全局对象进行初始化
 * Application Lifecycle Listener implementation class MyServletContextListener
 *
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {



	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

    
    public void contextInitialized(ServletContextEvent sce)  { 
    	// 所有在线用户数据集合
		Map<UserModel, HttpSession> map = new HashMap<UserModel, HttpSession>();
		// 将集合保存ServletContext 数据范围
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("map", map);
		System.out.println("服务器已启动，开启装逼模式，duang,duang,duang!!!");
    }
	
}
