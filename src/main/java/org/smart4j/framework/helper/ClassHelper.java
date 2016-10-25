package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

/**
 * encapsulation for ClassUtil 
 * get all classes from packages
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
	 * get all service classes by annotation "Service"
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
	 * get all controller objs by annotation "Controller"
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
	 * get all objs by annotation "Aspect"
	 * @return
	 */
	public static Set<Class <?>> getAspectsClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		
		for(Class<?> cls : CLASS_SET){
			if(cls.isAnnotationPresent(Aspect.class)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包下带有某注解的所有的类
	 * @param annotationClass
	 * @return
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls: CLASS_SET){
			if(cls.isAnnotationPresent(annotationClass)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取 应用包名下父类或接口的所有字累或实现类
	 * 使用方法 isAssignableFrom 判断加载的(类－对象) list 中是否有当前父类的子类或当前接口的实现类
	 * @param superClass
	 * @return
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		Set<Class<?>> classSet = new HashSet();
		for(Class<?> cls : CLASS_SET){
			if(superClass.isAssignableFrom(cls)&&!(superClass.equals(cls))){
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
