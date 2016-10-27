package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;

public final class AopHelper {

	public static final Logger logger = LoggerFactory.getLogger(AopHelper.class);
	
	static{
		try{
			//一个代理类(切面类)针对多个目标类
			Map<Class<?>, Set<Class<?>>> proxyMap = getProxyMap();
			//一个目标类 被一个 或者个多个 代理对象封装
			Map<Class<?>, List<Proxy>> targetMap = getTargetMap(proxyMap);
			
			for(Map.Entry<Class<?>, List<Proxy>> targetEntry:targetMap.entrySet()){
				Class<?> targetClass= targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();
				
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);
				
				BeanHelper.setBean(targetClass, proxy);
			}
			
		}catch(Exception e){
			logger.error("AOP fail", e);
		}
	}

	
	/**
	 * 一个切面类对应多个目标类
	 * @return
	 */
	private static Map<Class<?>, Set<Class<?>>> getProxyMap() {
		
		Map<Class<?>, Set<Class<?>>> targetMap = new HashMap<Class<?>, Set<Class<?>>>();
		//获得aspectproxy的所有的子类实现类
		Set<Class<?>> childrenClass = ClassHelper.getClassSetBySuper(AspectProxy.class);
		
		for(Class<?> proxyClass:childrenClass){
			//提取proxyClass中  注解为aspect的切面类
			if(proxyClass.isAnnotationPresent(Aspect.class)){
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				//获得自类中aspect注解的类中 需要添加的aspect 的目标类
				Set<Class<?>> targetclassset= createTargetClassSet(aspect);
				targetMap.put(proxyClass, targetclassset);
				
			}
		}
		return targetMap;
	}

	//通过注解Aspect 获得所有Aspect的value所表示的目标类的值
	private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
		Set<Class<?>> classset = new HashSet<Class<?>>();
		
		Class<? extends Annotation> cls = aspect.value();
		if( cls!=null && !cls.equals(Aspect.class)){
			classset.addAll(ClassHelper.getClassSetByAnnotation(cls));
		}
		
		return classset;
		
	}

	//一个目标类有一个或几个切面
	private static Map<Class<?>, List<Proxy>> getTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws InstantiationException, IllegalAccessException {
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		
		Set<Class<?>> classset = new HashSet<Class<?>>();
		
		for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry:proxyMap.entrySet()){
			Class<?> proxyClass = proxyEntry.getKey();
			
			classset = proxyEntry.getValue();
			
			for(Class<?> targetClass:classset){
				Proxy proxy = (Proxy) proxyClass.newInstance();
				if(targetMap.containsKey(targetClass)){
					targetMap.get(targetClass).add(proxy);
				}
				else{
					List<Proxy> proxyList = new ArrayList<Proxy>();
					proxyList.add(proxy);
					targetMap.put(targetClass, proxyList);
				}
			}
			
		}
		
		
		return targetMap;
	}
}
