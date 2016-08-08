package com.qg.util;

import java.sql.Connection;
 import java.sql.Driver;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;

 /**
  * JDBC数据库连接池
  * 
  * @author 
  * 
  */
 public class SimpleConnectionPool {
	 
	 //未使用的连接对象集合
	 private static LinkedList m_notUsedConnection = new LinkedList();
	 //正在使用的连接对象集合
     private static HashSet m_usedUsedConnection = new HashSet();
     /*
      * 数据库连接信息
      */
     private static String m_url = "jdbc:mysql://127.0.0.1:3306/qgzone";
     private static String m_user = "root";
     private static String m_password = "123456";
     private static int m_maxConnect = 3;
     
     //测试bug
     static final boolean DEBUG = false;
     
     static private long m_lastClearClosedConnection = System
             .currentTimeMillis();
     public static long CHECK_CLOSED_CONNECTION_TIME = 5000; // 5秒
 
     static {
         try {
             initDriver();
         } catch (InstantiationException e) {
             // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (ClassNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
     }
     /**
      * 构造函数
      * @param url 连接协议
      * @param user 用户名
      * @param password 密码
      */
     public SimpleConnectionPool(String url, String user, String password) {
         m_url = url;
         m_user = user;
         m_password = password;
     }
     /**
      * 加载数据库驱动
      * @throws InstantiationException
      * @throws IllegalAccessException
      * @throws ClassNotFoundException
      */
     private static void initDriver() throws InstantiationException,
             IllegalAccessException, ClassNotFoundException {
         Driver driver = null;
 
         // 读取MySql的Driver
         driver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
         installDriver(driver);
 
 
     }
 
     private static void installDriver(Driver driver) {
    	 
         try {
             DriverManager.registerDriver(driver);
         } catch (SQLException e) {
            // TODO Auto-generated catch block
             e.printStackTrace();
        }
     }
     /**
      * 公有方法：获取数据库连接对象
      * @return Connection对象
      */
     public static synchronized Connection getConnection() {
         // 关闭清除多余的连接
         clearClosedConnection();
 
         // 输出当前总连接数
         if(DEBUG)
             System.out.println("当前总连接数：" + getConnectionCount());
 
         // 寻找空闲的连接
         while (m_notUsedConnection.size() > 0) {
             try {
                 Connection con = (Connection) m_notUsedConnection.removeFirst();
                 if (con.isClosed()) {
                     continue;
                 }
 
                 m_usedUsedConnection.add(con);
                 if (DEBUG) {
                     // System.out.println("连接初始化成功");
               }
                 return con;
             } catch (SQLException e) {
             }
         }
 
         // 没有找到，建立一些新的连接以供使用
         int newCount = getIncreasingConnectionCount();
         LinkedList list = new LinkedList();
         Connection con = null;

         for (int i = 0; i < newCount; i++) {
             con = getNewConnection();
             if (con != null) {
                 list.add(con);
            }
         }
 
         // 没有成功建立连接，访问失败
         if (list.size() == 0)
             return null;

         // 成功建立连接，使用的加入used队列，剩下的加入notUsed队列
         con = (Connection) list.removeFirst();
         m_usedUsedConnection.add(con);
         m_notUsedConnection.addAll(list);
         list.clear();
          return con;
    }
     /**
      * 建立新连接对象
      * @return Connection对象
      */
     private static Connection getNewConnection() {
         try {
             Connection con = DriverManager.getConnection(m_url, m_user,
                     m_password);
             return con;
         } catch (SQLException e) {
             // TODO Auto-generated catch block    
        	 e.printStackTrace();
         }
 
         return null;
     }
     /**
      * 公有方法,将连接对象放回连接池
      * @param con  Connection对象
      */
     public static synchronized void pushConnectionBackToPool(Connection con) {
         boolean exist = m_usedUsedConnection.remove(con);
         if (exist) {
             m_notUsedConnection.addLast(con);
         }
     }
     /**
      * 公有方法,关闭并清除全部connection连接
      * @return 返回未被使用却被关闭连接的 Connection对象的数量
      */
     public static int close() {
         int count = 0;
 
         Iterator iterator = m_notUsedConnection.iterator();
         while (iterator.hasNext()) {
             try {
                ((Connection) iterator.next()).close();
                 count++;
             } catch (SQLException e) {
                 // TODO Auto-generated catch block
               e.printStackTrace();
             }
         }
         m_notUsedConnection.clear();
 
         iterator = m_usedUsedConnection.iterator();
         while (iterator.hasNext()) {
             try {
                 ((Connection) iterator.next()).close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
             }
         }
         m_usedUsedConnection.clear();
 
         return count;
     }
     /**
      * 清除未被使用而处于关闭状态的 Connection对象
      */
     private static void clearClosedConnection() {
         long time = System.currentTimeMillis();
 
         // 时间不合理，没有必要检查
         if (time < m_lastClearClosedConnection) {
             time = m_lastClearClosedConnection;
             return;
         }
 
         // 时间太短，没有必要检查
         if (time - m_lastClearClosedConnection < CHECK_CLOSED_CONNECTION_TIME) {
             return;
         }
 
         m_lastClearClosedConnection = time;
 
         // 开始检查没有使用的Connection
         Iterator iterator = m_notUsedConnection.iterator();
         while (iterator.hasNext()) {
            Connection con = (Connection) iterator.next();
 
             try {
                 if (con.isClosed()) {
                     iterator.remove();
                 }
             } catch (SQLException e) {
                 iterator.remove();
                  if (DEBUG) {
                     System.out.println("问题连接已断开");
                 }
            }         }
 
         // 清除多余的Connection
         int decrease = getDecreasingConnectionCount();
 
         while (decrease > 0 && m_notUsedConnection.size() > 0) {
             Connection con = (Connection) m_notUsedConnection.removeFirst();
 
             try {
                 con.close();
             } catch (SQLException e) {
 
             } 
             decrease--;         }     }
 
     
     
     /**
      * 获取需要增加连接的数量
      * @return 需要连接的数量
      */
     public static int getIncreasingConnectionCount() {
         int count = 1;
         count = getConnectionCount() / 4;
 
        if (count < 1)
             count = 1;
 
        return count;
     }
     /**
      * 获取需要清除连接的数量
      * @return 需要清除连接的数量
      */
     public static int getDecreasingConnectionCount() {
        int count = 0;
 
         if (getConnectionCount() > m_maxConnect) {
             count = getConnectionCount() - m_maxConnect;
         }
 
         return count;
    }
     /**
      * 获取未被使用的连接对象的数量
      * @return 未被使用的连接对象的数量
      */
     public static synchronized int getNotUsedConnectionCount() {
         return m_notUsedConnection.size();
     }
     /**
      * 获取已被使用的连接对象的数量
      * @return 已被使用的连接对象的数量
      */
     public static synchronized int getUsedConnectionCount() {
         return m_usedUsedConnection.size();
     }
     /**
      * 获取连接对象的总和
      * @return 连接对象的总和
      */
     public static synchronized int getConnectionCount() {
         return m_notUsedConnection.size() + m_usedUsedConnection.size();
     }
 
 }
