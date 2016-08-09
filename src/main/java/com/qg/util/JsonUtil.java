package com.qg.util;

import java.util.List;
/***
 * 
 * @author dragon
 *
 * @param <T>List对象
 * @param <K>实体对象
 * <pre>
 * 将List对象，实体对象和状态码打包
 * </pre>
 */

public class JsonUtil<T,K> {

	private List<T> jsonList;
	private int state;
	private K jsonObject;
	
	public JsonUtil(List<T> jsonList, int state, K jsonObject) {
		this.jsonList = jsonList;
		this.state = state;
		this.jsonObject = jsonObject;
	}
	public JsonUtil( int state,List<T> jsonList) {
		this.jsonList = jsonList;
		this.state = state;
	}
	public JsonUtil(int state, K jsonObject) {
		this.state = state;
		this.jsonObject = jsonObject;
	}
	public JsonUtil(int state) {
		super();
		this.state = state;
	}
	
	
}
