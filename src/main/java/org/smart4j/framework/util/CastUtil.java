package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;
/**
 * transfer obj to String, Double, Int, Boolean
 * @author huanghanwei
 *
 */
public final class CastUtil {

	/**
	 * transfer to string
	 * default value is null
	 * @param obj
	 * @return
	 */
	public static String castString(Object obj){
		return CastUtil.castString(obj, "");
	}
	
	/**
	 * transfer to string
	 * provide settings for default
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static String castString(Object obj, String defaultValue){
		if(obj!=null){
			return String.valueOf(obj);
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static int castInt(Object obj){
		return castInt(obj,0);
	}
	
	public static int castInt(Object obj, int defaultValue){
		int value = defaultValue;
		if(obj!=null){
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				value = Integer.parseInt(str);
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static long castLong(Object obj){
		return castLong(obj,0);
	}
	
	public static long castLong(Object obj, long defaultValue){
		long value = defaultValue;
		
		if(obj!=null){
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				value = Long.parseLong(str);
			}
		}
		
		return value;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static Double castDouble(Object obj){
		return castDouble(obj, 0);
	}
	
	public static Double castDouble(Object obj, double defaultValue){
		double value= defaultValue;
		if(obj!=null){
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				value = Double.parseDouble(str);
			}
			
		}
		return value;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean castBoolean(Object obj){
		return CastUtil.castBoolean(obj, false);
	}

	public static boolean castBoolean(Object obj, boolean defaultValue){
		boolean value = defaultValue;
		if(obj!=null){
			value = Boolean.parseBoolean(castString(obj));
		}
		return value;
	}
}
