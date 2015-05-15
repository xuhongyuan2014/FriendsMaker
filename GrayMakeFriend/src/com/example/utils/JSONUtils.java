package com.example.utils;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
	/*
	 * 转化json字符串中的data部分转化为entity
	 */
	public static <T> T jsonToEntity(String json, Class<T> clazz){
		return getJsonEntity(json, clazz);
	}
	
	/*
	 * 转化json字符串中的data部分转化为entity数组
	 */
	public static <T> List<T> jsonToEntityArray(String json, Class<T> clazz){
		return getJsonEntityArray(json, clazz);
	}
	
	/*
	 * 转化json字符串为map
	 */
	public static <T> Map<String, Object> jsonToMap(String json, Class<T> clazz){
		Map<String, Object> res = new HashMap<String, Object>();
		JSONObject obj = JSON.parseObject(json);
		
		for(String key : obj.keySet()){
			if(!"data".equals(key)) res.put( key , obj.get(key));
		}
		
		res.put("data", getJsonEntity(json, clazz));
		return res;
	}
	
	
	private static <T> T getJsonEntity(String json, Class<T> clazz){
		return JSON.parseObject(json).getObject("data", clazz);
	}
	
	private static <T> List<T> getJsonEntityArray(String json, Class<T> clazz){
		JSONArray data = JSON.parseObject(json).getJSONArray("data");
		if( data != null && !data.isEmpty() ) return JSON.parseArray(data.toJSONString(), clazz);
		else return null;
	}
	
	public static List<Map<String, Object>> getDataMap(String[] keys, String[] fields, String json){
		JSONArray data = JSON.parseObject(json).getJSONArray("data");
		List<Map<String ,Object>> list = new ArrayList<Map<String, Object>>();
		
		if(data == null || data.isEmpty()) return list;
		
		for(int j = 0; j<data.size(); j++){
			JSONObject obj = data.getJSONObject(j);
			
			Map<String, Object> item = new HashMap<String, Object>();
			for(int i = 0; i < keys.length; i++){
				if(keys[i].toLowerCase().contains("time") || keys[i].toLowerCase().contains("date")){
					item.put(keys[i], obj.getDate(fields[i]).toLocaleString());
				}else{
					item.put(keys[i] , obj.get(fields[i]));
				}
			}
			
			list.add(item);
		}
		
		return list;
	}
	
	
	
	public static List<Map<String, Object>> getDataList(String[] keys, String[] fields, String json){
		JSONArray data = JSON.parseObject(json).getJSONArray("data");
		List<Map<String ,Object>> list = new ArrayList<Map<String, Object>>();
		
		if(data == null || data.isEmpty()) return list;
		
		for(int j = 0; j<data.size(); j++){
			JSONObject obj = data.getJSONObject(j);
			
			Map<String, Object> item = new HashMap<String, Object>();
			for(int i = 0; i < keys.length; i++){
				if(keys[i].toLowerCase().contains("time") || keys[i].toLowerCase().contains("date")){
					item.put(keys[i], obj.getDate(fields[i]).toLocaleString());
				}else{
					item.put(keys[i] , obj.get(fields[i]));
				}
			}
			
			list.add(item);
		}
		
		return list;
	}
	
	public static <T> T test(String json, Class<T> clazz){
		return JSON.parseObject(json,clazz);
	}
}
