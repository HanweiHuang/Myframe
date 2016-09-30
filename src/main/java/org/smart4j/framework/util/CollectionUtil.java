package org.smart4j.framework.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * provide some operation about collection
 * actually it is a encapsulation for CollectionUtils
 * @author huanghanwei
 *
 */
public final class CollectionUtil {

	/**
	 * empty for collection
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection){
		return CollectionUtils.isEmpty(collection);
	}
	
	/**
	 * is not empty for collection
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection){
		return !isEmpty(collection);
	}
	
	/**
	 * empty for map
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map){
		return MapUtils.isEmpty(map);
	}
	
	/**
	 * is not empty for map
	 * @param map
	 * @return
	 */
	public static boolean isNotEmpty(Map<?, ?> map){
		return !isEmpty(map);
	}
}
