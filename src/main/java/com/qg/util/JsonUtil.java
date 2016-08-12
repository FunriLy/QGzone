package com.qg.util;

import java.util.List;

import com.google.gson.Gson;
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

public class JsonUtil<T> {

	private static Gson gson = null;
	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	public static <T, K> String tojson(int state, List<T> jsonList) {
		return gson.toJson(new ObjectModel<T, K>(state, jsonList));
	}

	public static <T, K> String tojson(int state, K jsonList) {
		return gson.toJson(new ObjectModel<T, K>(state, jsonList));
	}

	public static <T, K> String tojson(int state) {
		return gson.toJson(new ObjectModel<T, K>(state));
	}

}

class ObjectModel<T, K> {
	List<T> jsonList;
	int state;
	K jsonObject;

	public ObjectModel(int state, List<T> jsonList) {
		this.jsonList = jsonList;
		this.state = state;
	}

	public ObjectModel(int state) {
		this.state = state;
	}

	public ObjectModel(int state, K jsonObject) {
		this.state = state;
		this.jsonObject = jsonObject;
	}
}