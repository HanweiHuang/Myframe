package org.smart4j.framework.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtil {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 获得文件名
	 * @param fileName
	 * @return
	 */
	public static String getRealFileName(String fileName){
		return FilenameUtils.getName(fileName);
	}
	
	public static File createFile(String filePath){
		File file;
		try{
			file = new File(filePath);
			File parentDir = file.getParentFile();
			
			if(!parentDir.exists()){
				FileUtils.forceMkdir(parentDir);
			}
		}catch(Exception e){
			LOGGER.error("create file failure", e);
			throw new RuntimeException();
		}
		return file;
	}
}
