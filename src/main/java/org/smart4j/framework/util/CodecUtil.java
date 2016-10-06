package org.smart4j.framework.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CodecUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);
			
	/**
	 * url encode
	 * @param source
	 * @return
	 */
	public static String encodeURL(String source){
		String result;
		
		try{
			result = URLEncoder.encode(source,"UTF-8");
		}catch(Exception e){
			LOGGER.error("encode url failure",e);
			throw new RuntimeException();
		}
		return result;
	}
	
	/**
	 * url decoder
	 * @param source
	 * @return
	 */
	public static String decodeURL(String source){
		String result;
		try{
			result = URLDecoder.decode(source,"UTF-8");
		}catch(Exception e){
			throw new RuntimeException();
		}
		return result;
	}
}
