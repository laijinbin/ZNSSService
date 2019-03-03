package com.ittest.utils;

import java.util.HashMap;
import java.util.Map;

public class WebUtil {
	
	private final static String successCode = "0";
	private final static String paramErrorCode = "1";
	
	
	public static Map generateSuccessModelMap(){
		return generateModelMap(successCode,"成功");
	}
	
	public static Map generateFailModelMap(String failErrorMsg){
		return generateModelMap(paramErrorCode,failErrorMsg);
	}
	
	@SuppressWarnings("unchecked")
	public static Map generateModelMap(String code,String msg){
		Map map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("msg",msg);
		return map;
	}

}
