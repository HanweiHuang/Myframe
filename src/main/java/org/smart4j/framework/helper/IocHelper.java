package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * 1.iterator all beans
 * 2.use reflection to check every variation whether it has annotation Inject
 * 3.if it has, get the obj from the beans map and set it as the value of the variation by 
 * function setField from the class ReflectionUtil.
 * @author huanghanwei
 *
 */
public final class IocHelper {

	static{
		//1.get bean map
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		
		if(CollectionUtil.isNotEmpty(beanMap)){
			for(Map.Entry<Class<?>, Object> beanEntry: beanMap.entrySet()){
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//get all fields declared by the current class
				Field[] beanFields = beanClass.getDeclaredFields();
				if(ArrayUtil.isNotEmpty(beanFields)){
					for(Field beanfield : beanFields){
						//if a field has annotation inject
						if(beanfield.isAnnotationPresent(Inject.class)){
							Class<?> beanFieldClass = beanfield.getType();
							//get obj from beanMap by beanfieldclass
							Object beanFieldObject = beanMap.get(beanFieldClass);
							//change field value
							if(beanFieldObject != null){
								ReflectionUtil.setField(beanInstance, beanfield, beanFieldObject);
							}
						}
					}
				}
			}
		}
	}

}
