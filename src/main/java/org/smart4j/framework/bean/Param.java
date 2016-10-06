package org.smart4j.framework.bean;

import java.util.Map;

import org.smart4j.framework.util.CastUtil;

public class Param {
	
	private Map<String, Object> paramMap;
	
	public Param(Map<String, Object> paramMap){
		this.paramMap = paramMap;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	
	public long getLong(String name){
		return CastUtil.castLong(paramMap.get(name));
	}
}