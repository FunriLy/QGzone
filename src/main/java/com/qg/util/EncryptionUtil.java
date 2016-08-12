package com.qg.util;


import java.math.BigInteger;
import java.security.MessageDigest;
/*
 * 使用md5的算法进行加密
 */          
public class EncryptionUtil  {
	public static String md5(String password) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(password.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigInteger(1, secretBytes).toString(16);// 将加密后byte数组转换16进制表示
	}
}
