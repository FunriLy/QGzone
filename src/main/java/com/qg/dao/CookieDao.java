package com.qg.dao;

public interface CookieDao {
	
	/**
	 * 保存用户的sessionId
	 * @param userId 用户id
	 * @param sessionId sessId
	 * @return 成功返回success，否则返回fail
	 */
	int saveCookie(int userId, String sessionId);
	
	/**
	 * 更新sessionId时间 
	 * @param sessionId sessionId
	 * @return 成功返回success，否则返回fail
	 */
	int updateCookie(String sessionId);
	
	/**
	 * 移除sessionId
	 * @param sessionId sessionId
	 * @return 成功返回success，否则返回fail
	 */
	int removeCookie(String sessionId);
	
	/**
	 * 从sesionId获得用户id
	 * @param sessionId sessionId
	 * @return 若找到返回用户id，否则返回 0
	 */
	int getUserIdfromCookie(String sessionId);
	
	
}
