package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectionUtil;

/**
 * get all classes by classhelp and then use reflectionutil's new instance build all object.
 * sotre all objects in a map.
 * @author huanghanwei
 *
 */
public final class BeanHelper {

	private static final Map<Class<?>, Object> BEANS_MAP = new HashMap<Class<?>, Object>();
	
	static{
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for(Class<?> beanClass: beanClassSet){
			Object obj = ReflectionUtil.newInstance(beanClass);
			BEANS_MAP.put(beanClass, obj);
		}
	}
	
	/**
	 * get beans's Map
	 * @return
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEANS_MAP;
	}
	
	/**
	 * get a bean 
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(Class<?> cls){
		if(!BEANS_MAP.containsKey(cls)){
			throw new RuntimeException("Can not get bean by class:" + cls);
		}
		return (T)BEANS_MAP.get(cls);
	}
}
