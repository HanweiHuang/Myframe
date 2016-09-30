package org.smart4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * we have ClassHelp which can load class from the backage path.
 * But we can not build object by loaded classes.
 * This class provide some methods based on reflection API which will new objects for frame 
 * @author huanghanwei
 *
 */
public final class ReflectionUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
	
	/**
	 * get instance by class
	 * @param cls
	 * @return
	 */
	public static Object newInstance(Class<?> cls){
		Object instance = null;
		
		try {
			instance = cls.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("new instance failure",e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.error("new instance failure",e);
			e.printStackTrace();
		}
		
		return instance;
	}
	
	/**
	 * get object by method's invoke
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object invokeMethod(Object obj, Method method, Object... args){
		Object result;
		try {
			method.setAccessible(true);
			result = method.invoke(obj, args);
		} catch (Exception e) {
			LOGGER.error("invoke method failture", e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/**
	 * set value for field of a class
	 * @param obj
	 * @param field
	 * @param value
	 */
	public static void setField(Object obj, Field field, Object value){
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			LOGGER.error("set field failture",e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.error("set field failture",e);
			e.printStackTrace();
		}
	}
	
}
