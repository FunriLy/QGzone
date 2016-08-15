package com.qg.util.listener;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class OnlineCountHttpSessionListener
 *
 */
@WebListener
public class OnlineCountHttpSessionListener implements HttpSessionListener {




    public void sessionCreated(HttpSessionEvent sc)  { 
    	HttpSession session = sc.getSession();
		System.out.println(session.getId()+"创建");
    }


    public void sessionDestroyed(HttpSessionEvent sc)  { 
    	HttpSession session = sc.getSession();
    	
    	System.out.println(session.getId()+"销毁");
    }
	
}
