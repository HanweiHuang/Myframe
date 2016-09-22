package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class load util
 * @author huanghanwei
 *
 */
public final class ClassUtil {
	//set logger
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
	//get classloader first
	public static ClassLoader getClassLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * reutn Class<?> obj associated with className
	 * @param className
	 * @param isInitialized
	 * @return
	 */
	public static Class<?> loadClass(String className,boolean isInitialized){
		Class<?> cls;
		try {
			cls = Class.forName(className,isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			LOGGER.error("load class failure", e);
			e.printStackTrace();
			throw new RuntimeException();
		}
		return cls;
	}
}
