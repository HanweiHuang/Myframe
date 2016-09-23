package org.smart4j.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * read properties file
 * @author huanghanwei
 *
 */
public final class PropsUtil {

	/**
	 * RETURN AN LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
	
	/**
	 * 1.get inputstream from fileName
	 * 2.load stream
	 * @return
	 */
	public static Properties loadProps(String fileName){
		Properties props = null;
		InputStream is = null;
		
		try{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if(is == null){
				throw new FileNotFoundException(fileName + "file is not found");
			}
			props = new Properties();
			props.load(is);
			
		}catch(Exception e){
			LOGGER.error("load properties file failure", e);
			//e.printStackTrace();
		}finally{
			//close the inputstream
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("close input stream failure", e);
					e.printStackTrace();
				}
			}
		}
		return props;
	}
	
	/**
	 * getString, the default value is ""
	 * @param props
	 * @param key
	 * @return
	 */
	public static String getString(Properties props, String key){
		return getString(props, key, "");
	}
	
	/**
	 * getString can set default value
	 * @param props
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Properties props, String key, String defaultValue){
		String value = defaultValue;
		if(props.containsKey(key)){
			value = props.getProperty(key);
		}
		return value;		
	}
	
	/**
	 * getInt, the default value is 0 
	 * @param props
	 * @param key
	 * @return
	 */
	public static int getInt(Properties props, String key){
		return getInt(props,key,0);
	}
	
	public static int getInt(Properties props, String key, int defaultValue){
		int value = defaultValue;
		if(props.containsKey(key)){
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	
	/**
	 * getBoolean, the default value is false
	 * @param props
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Properties props, String key){
		return getBoolean(props, key, false);
	}
	
	public static boolean getBoolean(Properties props, String key, boolean defaultValue){
		boolean value = defaultValue;
		if(props.containsKey(key)){
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
	
}
