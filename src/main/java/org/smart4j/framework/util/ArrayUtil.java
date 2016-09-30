package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

public final class ArrayUtil {
	
	/**
	 * is empty for a array
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array){
		return ArrayUtils.isEmpty(array);
	}
	
	/**
	 * is not empty for a array
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(Object[] array){
		return !isEmpty(array);
	}
}
