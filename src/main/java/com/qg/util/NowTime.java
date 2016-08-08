package com.qg.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 
 * @author zggdczfr
 * <p>
 * 获取时间公用方法
 * </p>
 */


public class NowTime {
	
	/**
	 * 获取当前 Date 时间
	 * @return 当前 sql.Date 类型时间
	 */
	public static Date getCurrentDateTime(){
		long l = System.currentTimeMillis();
		Date date = new Date(l);
		return date;
	}
	
	/**
	 * 将 Date 型时间转化为(年-月-日 时-分-秒)字符串
	 * @param date Date型时间
	 * @return 字符串
	 */
	public static String getCurrentStrTime(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
}
