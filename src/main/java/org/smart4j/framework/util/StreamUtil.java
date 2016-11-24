package org.smart4j.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StreamUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);
	
	public static String getString(InputStream is){
		StringBuilder sb = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			String line;
			while((line=reader.readLine()) != null){
				sb.append(line);
			}
		}catch(Exception e){
			LOGGER.error("get string failure", e);
			throw new RuntimeException();
		}
		return sb.toString();
	}
	
	//把输入的inputstream 输出 提供了inputstram 和 outputstream了 就是write一下
	public static void copyStream(InputStream inputStream, OutputStream outputStream){
		try{
			int length;
			byte[] buffer = new byte[4*1024];
			while((length = inputStream.read(buffer,0,buffer.length))!=-1){
				outputStream.write(buffer,0,length);
			}
			
		}catch(Exception e){
			LOGGER.error("copy stream failure", e);
		}finally{
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				LOGGER.error("close stream failure",e);
				e.printStackTrace();
			}
			
		}
	}
}
