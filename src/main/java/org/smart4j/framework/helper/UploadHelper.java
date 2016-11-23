package org.smart4j.framework.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.spi.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StreamUtil;

public final class UploadHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
	
	/**
	 * Apache Commons FileUpload 提供的 Servlet 文件上传对象
	 */
	private static ServletFileUpload servletFileUpload;
	
	/**
	 * 1.设置上传文件尺寸
	 * 2.new servletFileUpload 对象
	 * @param servletContext
	 */
	public static void init(ServletContext servletContext){
		File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		servletFileUpload = new ServletFileUpload(
				new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
		
		int uploadlimit = ConfigHelper.getAppUploadLimit();
		if(uploadlimit != 0){
			servletFileUpload.setFileSizeMax(uploadlimit * 1024 * 1024);
		}
		
	}
	
	/**
	 * 判断请求是否是multipart
	 */
	
	/**
	 * 上传文件
	 * 提供了inputstram 和 outputstream 上传写入
	 *
	 */
	
	public static void uploadFile(String basePath, FileParam fileParam){
		
		try{
			if(fileParam!=null){
				String filePath = basePath + fileParam.getFileName();
				FileUtil.createFile(filePath);
			
				InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
			
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
				
				StreamUtil.copyStream(inputStream, outputStream);
			}
		}catch(Exception e){
			LOGGER.error("upload file failure", e);
			throw new RuntimeException();
		}
	}

	/**
	 * 添加判断是否是上传文件
	 * @param request
	 * @return
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}
}
