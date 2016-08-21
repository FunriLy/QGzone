package com.qg.util;

public class ParameterFormatCheck {

	
	/**
	 * 判断字符串是否只为数字
	 * @param string 字符串
	 * @return 是true 不是false
	 */
	public static boolean checkStringOfOnlyNumber(String string){
		String regex = "[0-9]+";
		if(string!=null&&string.matches(regex)){
			return true;
		}
		else{
			return false;
		}
	}
}
