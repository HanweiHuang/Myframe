package org.smart4j.framework.helper;

import java.util.HashSet;
import java.util.Set;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

/**
 * encapsulation for ClassUtil 
 * @author huanghanwei
 *
 */
public final class ClassHelper {

	private static final Set<Class<?>> CLASS_SET;
	
	/**
	 * load all class from base package
	 */
	static{
		String basePackage = ConfigHelper.getAppBasePackage();
		System.out.println(basePackage);
		CLASS_SET = ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * get all classes from base package 
	 * @return
	 */
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	
	/**
	 * get all service classes
	 * @return
	 */
	public static Set<Class<?>> getServiceClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		
		for(Class<?> cls : CLASS_SET){
			if(cls.isAnnotationPresent(Service.class)){
				classSet.add(cls);
			}
		}
		
		return classSet;
	}
	
	/**
	 * get all controller objs
	 * @return
	 */
	public static Set<Class<?>> getControllerClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls : CLASS_SET){
			if(cls.isAnnotationPresent(Controller.class)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * get all bean objs 
	 * @return
	 */
	public static Set<Class<?>> getBeanClassSet(){
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		
		beanClassSet.addAll(getServiceClassSet());
		beanClassSet.addAll(getControllerClassSet());
		
		return beanClassSet;
	}
	
}
