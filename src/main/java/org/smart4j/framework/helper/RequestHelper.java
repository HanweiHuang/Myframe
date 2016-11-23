package org.smart4j.framework.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

public final class RequestHelper {
	
	public static Param createParam(HttpServletRequest request) throws IOException{
		List<FormParam> formParamList = new ArrayList<FormParam>();
		formParamList.addAll(parseParameterNames(request));
		formParamList.addAll(parseInputStream(request));
		
		return new Param(formParamList);
	}
	
	/**
	 * request 获得参数名和值
	 * 参数名和值 一一对应生成FormParam对象，存入list，一个域名对应多个值的，以StringUtil的Separator区分
	 * 返回这个list
	 * @param request
	 * @return
	 */
	public static List<FormParam> parseParameterNames(HttpServletRequest request){
		List<FormParam> paramFormList = new ArrayList<FormParam>();
		
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String fieldName = paramNames.nextElement();
			String[] values = request.getParameterValues(fieldName);
			
			if(ArrayUtil.isNotEmpty(values)){
				Object fieldValue;
				if(values.length==1){
					fieldValue = values[0];
				}else{
					StringBuffer sb = new StringBuffer("");
					for(int i = 0; i<values.length;i++){
						sb.append(values[i]);
						if(i != values.length-1){
							sb.append(StringUtil.SEPARATOR);
						}
					}
					
					fieldValue = sb.toString();
				}
				
				paramFormList.add(new FormParam(fieldName, fieldValue));
			}//if
		}
		
		return paramFormList;
	}
	
	/**
	 * 从request 中获得inputstream 按照 & 分解，再按照 = 获得key 和 value
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException{
		
		List<FormParam> formParamList = new ArrayList<FormParam>();
		
		String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
		if(StringUtil.isNotEmpty(body)){
			String[] key_values = StringUtil.splitStream(body, "&");
			if(ArrayUtil.isNotEmpty(key_values)){
				for(String key_value: key_values){
					String[] array = StringUtil.splitStream(key_value, "=");
					if(ArrayUtil.isNotEmpty(array) && (array.length==2)){
						String fieldName = array[0];
						String fieldValue = array[1];
						formParamList.add(new FormParam(fieldName, fieldValue));
					}
				}
			}
		}
		return formParamList;
	}

}
